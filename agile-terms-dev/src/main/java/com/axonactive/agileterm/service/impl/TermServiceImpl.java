package com.axonactive.agileterm.service.impl;


import com.axonactive.agileterm.entity.DescriptionEntity;
import com.axonactive.agileterm.entity.TermEntity;
import com.axonactive.agileterm.entity.TopicEntity;
import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.exception.ErrorMessage;
import com.axonactive.agileterm.exception.ResourceNotFoundException;
import com.axonactive.agileterm.exception.SystemException;
import com.axonactive.agileterm.repository.DescriptionRepository;
import com.axonactive.agileterm.repository.TermRepository;
import com.axonactive.agileterm.repository.UserRepository;
import com.axonactive.agileterm.rest.client.model.Description;
import com.axonactive.agileterm.rest.client.model.Term;
import com.axonactive.agileterm.rest.client.model.TermName;
import com.axonactive.agileterm.rest.model.ResponseForUploadFile;
import com.axonactive.agileterm.rest.model.TermDto;
import com.axonactive.agileterm.service.DescriptionService;
import com.axonactive.agileterm.service.TermService;
import com.axonactive.agileterm.service.TermTopicService;
import com.axonactive.agileterm.service.UserService;
import com.axonactive.agileterm.service.dto.RowResultDto;
import com.axonactive.agileterm.service.dto.TermImportDto;
import com.axonactive.agileterm.service.mapper.TermMapper;
import com.axonactive.agileterm.utility.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TermServiceImpl implements TermService {

    @Autowired
    private ErrorMessage errorMessage;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private TermMapper termMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Autowired
    private DescriptionService descriptionService;
    @Autowired
    private TermTopicService termTopicService;
    @Autowired
    private UserService userService;


    public boolean isDescriptionListValid(Term termRequest) {
        return Boolean.TRUE.equals(isDescriptionRequestListNotEmpty(termRequest)) && null != termRequest.getDescriptionList();
    }

    @Override
    public List<String> get5TermNameLike(String searchInput) {
        return termRepository.get5TermNameLike(searchInput.replace("*", " "));
    }

    @Override
    public Boolean isExistedTermNameInDatabase(String searchInput) {
        return termRepository.countTermByTermName(searchInput) != 0;
    }

    @Override
    public void validateNewTermName(String termName) {
        if (isExistedTermNameInDatabase(termName.replace("*", " "))) {
            throw new SystemException(errorMessage.termExistedInDatabase);
        }
    }



    @Override
    public List<TermDto> getTop10PopularTerms() {
        List<TermEntity> top10PopularTermsWith1MostUpvoteDescription = new ArrayList<>();

        List<Integer> top10PopularTermIds = termRepository.get10PopularTermsId();
        List<TermEntity> top10PopularTermEntityDetails = termRepository.findTermsByIdList(top10PopularTermIds);
        Comparator<TermEntity> termEntityComparator= Comparator
                .comparing(TermEntity::getDescriptionEntityList,Comparator.comparing(List::size))
                .thenComparing(TermEntity::getDescriptionEntityList,Comparator.comparing(descriptionEntities -> descriptionEntities.stream().mapToInt(DescriptionEntity::getVotePoint).sum()))
                .thenComparing(TermEntity::getId).reversed();

        List<TermEntity> top10PopularTerms=top10PopularTermEntityDetails.stream().sorted(termEntityComparator).collect(Collectors.toList());

        for(TermEntity term:top10PopularTerms){
            Optional<DescriptionEntity> mostUpvoteDescription = term.getDescriptionEntityList().stream().max(Comparator.comparing(DescriptionEntity::getVotePoint));
            TermEntity tempTermEntity = new TermEntity();
            tempTermEntity.setId(term.getId());
            tempTermEntity.setName(term.getName());
            if (mostUpvoteDescription.isPresent()) {
                tempTermEntity.setDescriptionEntityList(List.of(mostUpvoteDescription.get()));
            } else {
                tempTermEntity.setDescriptionEntityList(new ArrayList<>());
            }
            top10PopularTermsWith1MostUpvoteDescription.add(tempTermEntity);
        }
        return termMapper.toDtos(top10PopularTermsWith1MostUpvoteDescription);
    }

    @Override
    public Boolean isDescriptionRequestListNotEmpty(Term term) {
        return null != term.getDescriptionList() &&
                !term.getDescriptionList().isEmpty();
    }

    @Override
    public TermDto update(Integer id, TermName termNameRequest) {
        TermEntity updatedTerm = termRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(errorMessage.termNotFoundMsg));
        updatedTerm.setName(termNameRequest.getName());
        return termMapper.toDto(termRepository.save(updatedTerm));
    }

    @Override
    public TermDto findTermByTermId(Integer id) {
        return termMapper.toDto(termRepository.findTermByTermId(id).orElseThrow(() -> new ResourceNotFoundException(errorMessage.termNotFoundMsg)));
    }

    @Override
    public List<TermDto> getAllQuery() {
        return termMapper.toDtos(termRepository.getAllQuery());
    }

    public ResponseForUploadFile uploadTermAndDescriptionExcelFile(MultipartFile multipartFile) throws IOException {
        List<TermImportDto> uploadedRawDataTermAndDescriptionList = ExcelUtils.getListOfTermAndDescriptionImportDto(multipartFile);

        RowResultDto rowResult = getRowResultFromRawDataList(uploadedRawDataTermAndDescriptionList);

        Map<String, Integer> successfullySavedMap = saveAllListOfValidRows(rowResult);

        return new ResponseForUploadFile(rowResult.getInvalidRows(), successfullySavedMap, getStopCountingRow(uploadedRawDataTermAndDescriptionList));
    }

    @Override
    public TermDto save(Term termRequest) {
        Optional<TermEntity> termFoundWithName = findTermByTermName(termRequest.getName());
        if (termFoundWithName.isPresent()) {

            TermDto updatedTermWithNewDescriptionDto = updateTermWithNewContent(termFoundWithName.get(), termRequest);
            //updatedTermWithNewDescriptionDto.setTopicList(termTopicService.findTopicByTermId(updatedTermWithNewDescriptionDto.getId()));
            return updatedTermWithNewDescriptionDto;
        }


        TermEntity termEntity = new TermEntity();
        termEntity.setName(termRequest.getName());
        if (descriptionService.isDescriptionListValid(termRequest.getDescriptionList())) {
            termEntity.setDescriptionEntityList(
                    descriptionService
                            .convertListOfDescriptionToListOfDescriptionEntity(
                                    termRequest.getDescriptionList(), termEntity));
        }
        TermEntity savedTerm = termRepository.save(termEntity);
        if (termTopicService.isTopicListValid(termRequest.getTopicIdList())) {
            termTopicService.saveAllTermTopic(savedTerm, termRequest.getTopicIdList());
        }
        return termMapper.toDto(savedTerm);
    }

    public Optional<TermEntity> findTermByTermName(String termName) {
        return termRepository.findTermByTermName(termName);
    }

    public TermDto updateTermWithNewContent(TermEntity termFound, Term term) {
        if (descriptionService.isDescriptionListValid(term.getDescriptionList())) {
            List<Description> listOfNewDescription = validateListOfDescription(termFound, term);
            saveNewDescriptionWithExistedTermByUser(termFound.getName(), listOfNewDescription);
        }

        List<Integer> listOfValidInputTopicId = getListOfValidInputTopicId(termFound, term);
        termTopicService.saveAllTermTopic(termFound, listOfValidInputTopicId);
        return findTermByTermId(termFound.getId());
    }

    public List<DescriptionEntity> saveNewDescriptionWithExistedTermByUser(String termName, List<Description> descriptionList) {

        List<DescriptionEntity> newDescriptionEntityList = new ArrayList<>();

        TermEntity term = findTermByTermName(termName).orElseThrow(() -> new ResourceNotFoundException(errorMessage.termNotFoundMsg));

        for (Description description : descriptionList
        ) {
            if (!isStringEmpty(description.getContent())) {
                newDescriptionEntityList.add(new DescriptionEntity(null, description.getContent(), LocalDate.now(), term,
                        userService.findUserEntityByUserName(description.getUserName()), 0));
            }

        }
        return descriptionService.saveAll(newDescriptionEntityList);
    }

    public List<Description> validateListOfDescription(TermEntity termFound, Term term) {
        List<String> listOfDescriptionInTerm = getListOfDescriptionInTerm(termFound);
        for (Description description : term.getDescriptionList()
        ) {
            if (listOfDescriptionInTerm.contains(description.getContent().toLowerCase(Locale.ROOT))) {
                throw new SystemException(errorMessage.descriptionExistedInDatabase);
            } else if (description.getContent() == null || description.getContent().equals(""))
                throw new SystemException(errorMessage.descriptionMustNotNull);
        }
        return term.getDescriptionList();
    }

    public List<String> getListOfDescriptionInTerm(TermEntity termFound) {
        return termFound.getDescriptionEntityList().stream().map(DescriptionEntity::getContent).map(String::toLowerCase).collect(Collectors.toList());
    }

    public List<Integer> getListOfValidInputTopicId(TermEntity termFound, Term term) {
        List<Integer> listOfAllTopicIdFromTerm = getListOfAllTopicIdFromTerm(termFound);
        List<Integer> listOfNewTopic = new ArrayList<>();

        for (Integer id : term.getTopicIdList()
        ) {
            if (!listOfAllTopicIdFromTerm.contains(id))
                listOfNewTopic.add(id);
        }
        return listOfNewTopic;
    }

    public List<Integer> getListOfAllTopicIdFromTerm(TermEntity termFound) {
        return termTopicService.findListOfTopicEntityFromTermId(termFound.getId()).stream().map(TopicEntity::getId).collect(Collectors.toList());
    }

    public RowResultDto getRowResultFromRawDataList(List<TermImportDto> rawDataList) {
        RowResultDto rowResultDto = new RowResultDto();
        List<TermImportDto> syntaxValidImportTermList = new ArrayList<>();
        for (TermImportDto importingTerm : rawDataList) {
            String importingTermString = importingTerm.getTermString();
            int importingTermRowIndex = importingTerm.getRowIndex();
            if (isStringEmpty(importingTermString)) {
                handleNullTermString(rowResultDto, importingTermRowIndex);

            } else if (isTermStringLengthInvalid(importingTermString)) {
                handleTermLengthInvalid(rowResultDto, importingTermRowIndex);

            } else if (isDescriptionLengthInvalid(importingTerm.getDescriptionString())) {
                handleDescriptionLengthInvalid(rowResultDto, importingTermRowIndex);

            } else if (isRowDuplicated(rowResultDto, importingTerm, syntaxValidImportTermList)) {
                handleDuplicatedRow(rowResultDto, importingTerm);

            } else if (isTermNotExistedInDatabase(importingTermString)) {
                rowResultDto.getValidRowNotExistedTermInDataBase().add(importingTerm);

            } else if (isDescriptionExistedInDataBase(importingTermString, importingTerm.getDescriptionString())) {
                handleRowExistedInDatabase(rowResultDto, importingTerm, syntaxValidImportTermList);

            } else {
                rowResultDto.getValidRowExistedTermInDataBase().add(importingTerm);
                syntaxValidImportTermList.add(importingTerm);
            }
        }
        return rowResultDto;
    }


    public Map<String, Integer> saveAllListOfValidRows(RowResultDto rowResult) {
        Map<String, List<String>> existedTermWithNewDescriptionToBeSave = ExcelUtils.getTermAndDefinitionMap(rowResult.getValidRowExistedTermInDataBase());
        Map<String, List<String>> nonExistedTermToBeSave = ExcelUtils.getTermAndDefinitionMap(rowResult.getValidRowNotExistedTermInDataBase());

        Map<String, Integer> successfullySavedMap = new HashMap<>();


        for (Map.Entry<String, List<String>> entry : existedTermWithNewDescriptionToBeSave.entrySet()) {
            List<DescriptionEntity> descriptionList = saveNewDescriptionWithExistedTerm(entry.getKey(), entry.getValue());
            successfullySavedMap.put(entry.getKey(), descriptionList.size());
        }

        for (Map.Entry<String, List<String>> entry : nonExistedTermToBeSave.entrySet()) {
            TermEntity savedTerm = saveNewTermNewDescription(entry.getKey(), entry.getValue());
            successfullySavedMap.put(savedTerm.getName(), savedTerm.getDescriptionEntityList().size());
        }

        return successfullySavedMap;
    }

    public TermEntity saveNewTermNewDescription(String termName, List<String> descriptionContentList) {
        UserEntity admin = userRepository.findById(1).orElseThrow(() -> new ResourceNotFoundException(errorMessage.userNotFoundMsg));

        List<DescriptionEntity> descriptionList = new ArrayList<>();

        TermEntity newTerm = new TermEntity();


        for (String descriptionContent : descriptionContentList
        ) {
            if (!isStringEmpty(descriptionContent)) {
                descriptionList.add(new DescriptionEntity(null, descriptionContent, LocalDate.now(), newTerm, admin, 0));
            }
        }

        newTerm.setName(termName);

        newTerm.setDescriptionEntityList(descriptionList);

        return termRepository.save(newTerm);
    }

    public List<DescriptionEntity> saveNewDescriptionWithExistedTerm(String termName, List<String> descriptionContentList) {
        UserEntity admin = userRepository.findById(1).orElseThrow(() -> new ResourceNotFoundException(errorMessage.userNotFoundMsg));
        List<DescriptionEntity> descriptionList = new ArrayList<>();
        TermEntity term = termRepository.findTermByTermName(termName).orElseThrow(() -> new ResourceNotFoundException(errorMessage.termNotFoundMsg));

        for (String descriptionContent : descriptionContentList
        ) {
            if (!isStringEmpty(descriptionContent))
                descriptionList.add(new DescriptionEntity(null, descriptionContent, LocalDate.now(), term, admin, 0));
        }
        return descriptionRepository.saveAll(descriptionList);
    }


    public void handleRowExistedInDatabase(RowResultDto rowResultDto, TermImportDto termImportDto, List<TermImportDto> validList) {
        rowResultDto.getInvalidRows().getExistedInTheDatabase().add(termImportDto.getRowIndex());
        validList.add(termImportDto);
    }

    public boolean isTermNotExistedInDatabase(String termName) {
        Optional<TermEntity> term = termRepository.findTermByTermName(termName);
        return term.isEmpty();
    }

    public boolean isDescriptionExistedInDataBase(String termName, String descriptionString) {
        if (isStringEmpty(descriptionString)) {
            return true;
        }
        List<DescriptionEntity> descriptionList = descriptionService.findDescriptionByTermIdAndDescriptionString(termName, descriptionString);
        return !descriptionList.isEmpty();
    }

    public void handleDuplicatedRow(RowResultDto rowResultDto, TermImportDto termImport) {
        rowResultDto.getInvalidRows().getDuplicatedDescriptionInFile().add(termImport.getRowIndex());
    }

    public boolean isRowDuplicated(RowResultDto rowResultDto, TermImportDto termImport, List<TermImportDto> syntaxValidList) {
        return rowResultDto.getValidRowExistedTermInDataBase().contains(termImport) || syntaxValidList.contains(termImport) || rowResultDto.getValidRowNotExistedTermInDataBase().contains(termImport);
    }

    public void handleDescriptionLengthInvalid(RowResultDto rowResultDto, int rowIndex) {
        rowResultDto.getInvalidRows().getInvalidDescriptionLength().add(rowIndex);
    }

    public boolean isDescriptionLengthInvalid(String descriptionString) {
        return descriptionString.length() > ExcelUtils.DESCRIPTION_MAXIMUM_LENGTH;
    }


    public void handleNullTermString(RowResultDto rowResultDto, int rowIndex) {
        rowResultDto.getInvalidRows().getTermIsNull().add(rowIndex);
    }

    public boolean isStringEmpty(String string) {
        return "".equalsIgnoreCase(string) || string.isBlank() || string.isEmpty();
    }

    public void handleTermLengthInvalid(RowResultDto rowResultDto, int rowIndex) {
        rowResultDto.getInvalidRows().getInvalidTermLength().add(rowIndex);
    }

    public boolean isTermStringLengthInvalid(String termImport) {
        return !(termImport.length() >= ExcelUtils.TERM_MINIMUM_LENGTH && termImport.length() <= ExcelUtils.TERM_TOPIC_MAXIMUM_LENGTH);
    }

    public int getStopCountingRow(List<TermImportDto> rawDataList) {
        return rawDataList != null && !rawDataList.isEmpty() ?
                rawDataList.get(rawDataList.size() - 1).getRowIndex() : 1;
    }

    @Override
    public List<TermDto> find10RecentTerms() {
        Pageable top10Terms = PageRequest.of(0,10);
        return termMapper.toDtos(termRepository.find10RecentTerms(top10Terms));
    }


}

