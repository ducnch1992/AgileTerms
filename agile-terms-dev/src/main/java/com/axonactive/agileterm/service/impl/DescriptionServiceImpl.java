package com.axonactive.agileterm.service.impl;

import com.axonactive.agileterm.entity.DescriptionEntity;
import com.axonactive.agileterm.entity.TermEntity;
import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.exception.ErrorMessage;
import com.axonactive.agileterm.exception.ResourceNotFoundException;
import com.axonactive.agileterm.repository.DescriptionRepository;
import com.axonactive.agileterm.repository.TermRepository;
import com.axonactive.agileterm.repository.UserRepository;
import com.axonactive.agileterm.rest.client.model.Description;
import com.axonactive.agileterm.rest.model.DescriptionDto;
import com.axonactive.agileterm.service.DescriptionService;
import com.axonactive.agileterm.service.UserService;
import com.axonactive.agileterm.service.mapper.DescriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DescriptionServiceImpl implements DescriptionService {
    @Autowired
    private ErrorMessage errorMessage;
    @Autowired
    private TermRepository termRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Autowired
    private DescriptionMapper descriptionMapper;

    @Autowired
    UserService userService;

    @Override
    public List<DescriptionDto> getAll() {
        return descriptionMapper.toDtos(descriptionRepository.findAll());
    }

    @Override
    public DescriptionDto findById(Integer id) {
        return descriptionMapper.toDto(descriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(errorMessage.descriptionNotFoundMsg)));
    }

    @Override
    public DescriptionDto save(Integer termId, Description descriptionRequest) {
        TermEntity requestTerm = termRepository.findById(termId).orElseThrow(() -> new ResourceNotFoundException(errorMessage.termNotFoundMsg));
        UserEntity requestUserEntity = userRepository.findByUsername(descriptionRequest.getUserName()).orElseThrow(() -> new ResourceNotFoundException(errorMessage.userNotFoundMsg));
        DescriptionEntity description = new DescriptionEntity();
        description.setContent(descriptionRequest.getContent());
        description.setTerm(requestTerm);
        description.setUserEntity(requestUserEntity);
        description.setCreateDate(LocalDate.now());

        return descriptionMapper.toDto(descriptionRepository.save(description));
    }

    @Override
    public void deleteById(Integer id) {
        descriptionRepository.deleteById(id);
    }

    @Override
    public DescriptionDto updateById(Integer termId, Description descriptionRequest, Integer id) {
        DescriptionEntity description = descriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage.descriptionNotFoundMsg));
        TermEntity requestTerm = termRepository.findById(termId).orElseThrow(() -> new ResourceNotFoundException(errorMessage.termNotFoundMsg));
        UserEntity requestUserEntity = userRepository.findByUsername(descriptionRequest.getUserName()).orElseThrow(() -> new ResourceNotFoundException(errorMessage.userNotFoundMsg));
        description.setContent(descriptionRequest.getContent());
        description.setCreateDate(LocalDate.now());
        description.setTerm(requestTerm);
        description.setUserEntity(requestUserEntity);

        return descriptionMapper.toDto(descriptionRepository.save(description));
    }

    @Override
    public List<DescriptionDto> findByTermIdOrderByVotePointDescCreateDateDesc(Integer id) {
        return descriptionMapper.toDtos(descriptionRepository.findByTermIdOrderByVotePointDescCreateDateDesc(id));
    }

    @Override
    public List<String> findDescriptionContentByTermName(String name) {
        return descriptionRepository.findDescriptionContentByTermName(name);
    }

    @Override
    public List<DescriptionEntity> convertDescriptionContentListToDescriptionList(Set<String> contentDescriptionList, TermEntity term, UserEntity userEntity) {
        return contentDescriptionList.stream()
                .map(content -> new DescriptionEntity(null, content, LocalDate.now(), term, userEntity, 0)).collect(Collectors.toList());

    }

    @Override
    public List<DescriptionEntity> findDescriptionByTermIdAndDescriptionString(String termString, String descriptionContent) {
        return descriptionRepository.findDescriptionByTermIdAndDescriptionString(termString, descriptionContent);
    }

    @Override
    public boolean isDescriptionListValid(List<Description> descriptionList) {
        return descriptionList != null &&
                !descriptionList.isEmpty();
    }

    @Override
    public List<DescriptionEntity> convertListOfDescriptionToListOfDescriptionEntity(List<Description> descriptionList, TermEntity term) {
        List<DescriptionEntity> descriptionEntityList = new ArrayList<>();
        for (Description inputDescription : descriptionList
        ) {
            if (!inputDescription.getContent().trim().isEmpty()) {
                descriptionEntityList.add(convertDescriptionToDescriptionEntity(inputDescription, term));
            }
        }
        return descriptionEntityList;
    }

    @Override
    public DescriptionEntity convertDescriptionToDescriptionEntity(Description description, TermEntity term) {
        DescriptionEntity descriptionEntity = new DescriptionEntity();
        descriptionEntity.setUserEntity(userService.findUserEntityByUserName(description.getUserName()));
        descriptionEntity.setContent(description.getContent());
        descriptionEntity.setTerm(term);
        return descriptionEntity;
    }

    @Override
    public List<DescriptionEntity> saveAll(List<DescriptionEntity> descriptionEntityList) {
        return descriptionRepository.saveAllAndFlush(descriptionEntityList);
    }
}
