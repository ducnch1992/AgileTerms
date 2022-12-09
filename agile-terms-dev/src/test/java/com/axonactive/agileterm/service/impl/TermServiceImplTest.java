package com.axonactive.agileterm.service.impl;

import com.axonactive.agileterm.entity.DescriptionEntity;
import com.axonactive.agileterm.entity.TermEntity;
import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.exception.ErrorMessage;
import com.axonactive.agileterm.exception.ResourceNotFoundException;
import com.axonactive.agileterm.exception.SystemException;
import com.axonactive.agileterm.repository.TermRepository;
import com.axonactive.agileterm.repository.TermTopicRepository;
import com.axonactive.agileterm.repository.UserRepository;
import com.axonactive.agileterm.rest.client.model.Description;
import com.axonactive.agileterm.rest.client.model.Term;
import com.axonactive.agileterm.rest.client.model.TermName;
import com.axonactive.agileterm.rest.client.model.Topic;
import com.axonactive.agileterm.rest.model.ResponseForUploadFile;
import com.axonactive.agileterm.rest.model.TermDto;
import com.axonactive.agileterm.service.TermService;
import com.axonactive.agileterm.service.dto.RowResultDto;
import com.axonactive.agileterm.service.dto.TermImportDto;
import com.axonactive.agileterm.utility.ExcelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("unit-test")
@Slf4j
@RequiredArgsConstructor
class TermServiceImplTest {
    @Autowired
    TermService termService;

    @Autowired
    TermServiceImpl termServiceImpl;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TermRepository termRepository;

    @Autowired
    TopicServiceImpl topicServiceImpl;

    @Autowired
    TermTopicRepository termTopicRepository;

    @Autowired
    private ErrorMessage errorMessage;

    private final static String PATH = "./src/test/resources/";

    public static final String SRC_TEST_RESOURCES_TERM_TEST_MULTIPLE_SCENARIO = PATH + "TERM_TEST_Multiple_Scenario.xlsx";

    private final FileTypeMap fileTypeMap = FileTypeMap.getDefaultFileTypeMap();

    private MultipartFile getMultipartFile(String pathName, String originalName) throws IOException {
        File file = new File(pathName);
        FileInputStream fileInputStream = new FileInputStream(file);
        String fileType = fileTypeMap.getContentType(file);
        return new MockMultipartFile(originalName, file.getName(), fileType, fileInputStream);
    }

    Sheet getSheet(MultipartFile multipartFile) throws IOException {
        Workbook workbook = ExcelUtils.convertToWorkBook(multipartFile);
        return workbook.getSheetAt(ExcelUtils.FIRST_SHEET);
    }


    @BeforeEach
    void setup() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setUsername("Huy");
        userEntity1.setEmail("admin@axonactive.com.vn");
        userEntity1.setPassword("Aavn123!@#");

        userRepository.save(userEntity1);
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setUsername("Quang");
        userEntity2.setEmail("admn@axonactive.com.vn");
        userEntity2.setPassword("Aavn123!@#");
        userRepository.save(userEntity2);
        Topic agileTopic = Topic.builder()
                .name("agile")
                .build();
        topicServiceImpl.save(agileTopic);

        Topic programingTopic = Topic.builder()
                .name("programing")
                .build();
        topicServiceImpl.save(programingTopic);

        Term newTerm = Term.builder()
                .name("Dod")
                .build();
        termService.save(newTerm);
        Term scrum = Term.builder()
                .name("Scrum")
                .build();
        termService.save(scrum);
    }

    @Test
    void testFindTermByTermId_shouldReturnNameDOD_whenInput1() {
        assertEquals("Dod", termService.findTermByTermId(1).getName());
    }

    @Test
    void testSave_shouldReturn2_whenSaveANewTerm() {

        Term term2 = new Term();
        term2.setName("Dinh");

        List<Description> descriptionList = new ArrayList<>();

        descriptionList.add(new Description("abc", "Huy", 0));
        descriptionList.add(new Description("def", "Huy", 0));
        term2.setDescriptionList(descriptionList);

        TermDto createdTerm = termService.save(term2);

        assertEquals(3, termService.getAllQuery().size());
        assertEquals("Dinh", createdTerm.getName());
        assertEquals(2, createdTerm.getDescriptionList().size());
    }

    @Test
    void testUpdate_shouldReturnNewTerm_whenUpdatedExistedTerm() {
        TermName termRequest = new TermName("Agile");
        termService.update(1, termRequest);
        assertEquals("Agile", termService.findTermByTermId(1).getName());
    }

    @Test
    void testFindById_shouldThrowResourceNotFoundException_whenInputNonExistedId() {
        try {
            termService.findTermByTermId(-1);
        } catch (ResourceNotFoundException e) {
            assertEquals(errorMessage.termNotFoundMsg, e.getMessage());
        }

    }


    @ParameterizedTest
    @ValueSource(strings = {"a", "", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void testIsTermStringLengthInvalid_shouldReturnTrue_whenInputInvalidString(String testString) {
        assertTrue(termServiceImpl.isTermStringLengthInvalid(testString));
    }


    @Test
    void testIsStringEmpty_shouldReturnTrue_whenInputEmptyString() {
        String testString = "";
        assertTrue(termServiceImpl.isStringEmpty(testString));
    }

    @Test
    void testIsStringEmpty_shouldReturnFalse_whenInputCharacters() {
        String testString = "abc";
        assertFalse(termServiceImpl.isStringEmpty(testString));
    }

    @Test
    void testIsDescriptionLengthInvalid_shouldReturnFalse_whenInputCharacters() {
        String testString = "abc";
        assertFalse(termServiceImpl.isDescriptionLengthInvalid(testString));
    }

    @Test
    void testIsDescriptionLengthInvalid_shouldReturnTrue_whenInputOver1000Characters() {
        String testString = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertTrue(termServiceImpl.isDescriptionLengthInvalid(testString));
    }

    @Test
    void testIsTermNotExistedInDatabase_shouldReturnTrue_whenInputNonExistedTerm() {
        assertTrue(termServiceImpl.isTermNotExistedInDatabase("rum"));
    }

    @Test
    void testIsTermNotExistedInDatabase_shouldReturnFalse_whenInputExistedTerm() {
        Term newTermScrum = Term.builder()
                .name("rum")
                .build();
        termService.save(newTermScrum);
        assertFalse(termServiceImpl.isTermNotExistedInDatabase("Scrum"));
    }

    @Test
    void testIsDescriptionExistedInDataBase_shouldReturnTrue_whenInputExistedDescription() {
        Term term2 = new Term();
        term2.setName("rum");
        List<Description> descriptionContentList = new ArrayList<>();
        descriptionContentList.add(new Description("abc", "Huy", 0));
        term2.setDescriptionList(descriptionContentList);

        termService.save(term2);
        assertTrue(termServiceImpl.isDescriptionExistedInDataBase("rum", "abc"));
    }

    @Test
    void testIsDescriptionExistedInDataBase_shouldReturnFalse_whenInputNonExistedDescription() {
        Term term2 = new Term();
        term2.setName("rum");

        List<Description> descriptionContentList = new ArrayList<>();
        Description description1 = new Description("abc", "Huy", 0);

        Description description2 = new Description("", "Huy", 0);
        term2.setDescriptionList(descriptionContentList);
        termService.save(term2);
        assertFalse(termServiceImpl.isDescriptionExistedInDataBase("Scrum", "xyz"));
        assertTrue(termServiceImpl.isDescriptionExistedInDataBase("Scrum", ""));
    }


    @Test
    void testHandleTermLengthInvalid_shouldReturn1_whenUse() {
        RowResultDto rowResultDto = new RowResultDto();
        termServiceImpl.handleTermLengthInvalid(rowResultDto, 1);
        assertEquals(1, rowResultDto.getInvalidRows().getInvalidTermLength().size());
    }

    @Test
    void testHandleNullTermString_shouldReturn1_whenUse() {
        RowResultDto rowResultDto = new RowResultDto();
        termServiceImpl.handleNullTermString(rowResultDto, 1);
        assertEquals(1, rowResultDto.getInvalidRows().getTermIsNull().size());
    }

    @Test
    void testHandleDescriptionLengthInvalid_shouldReturn1_whenUse() {
        RowResultDto rowResultDto = new RowResultDto();
        termServiceImpl.handleDescriptionLengthInvalid(rowResultDto, 1);
        assertEquals(1, rowResultDto.getInvalidRows().getInvalidDescriptionLength().size());
    }

    @Test
    void testHandleDuplicatedRow_shouldReturn1_whenUse() {
        RowResultDto rowResultDto = new RowResultDto();
        TermImportDto termImportDto = new TermImportDto();
        termServiceImpl.handleDuplicatedRow(rowResultDto, termImportDto);
        assertEquals(1, rowResultDto.getInvalidRows().getDuplicatedDescriptionInFile().size());
    }

    @Test
    void testHandleRowExistedInDatabase_shouldReturn1_whenUse() {
        RowResultDto rowResultDto = new RowResultDto();
        TermImportDto termImportDto = new TermImportDto();
        List<TermImportDto> termImportDtoList = new ArrayList<>();
        termServiceImpl.handleRowExistedInDatabase(rowResultDto, termImportDto, termImportDtoList);
        assertEquals(1, rowResultDto.getInvalidRows().getExistedInTheDatabase().size());
    }

    @Test
    void testIsRowDuplicated_shouldReturnTrue_whenInputExistedTermInDataBase() {
        RowResultDto rowResultDto = new RowResultDto();
        TermImportDto termImportDto = new TermImportDto(1, "dod", "definition of done");
        List<TermImportDto> syntaxValidList = new ArrayList<>();
        rowResultDto.getValidRowExistedTermInDataBase().add(termImportDto);
        termServiceImpl.isRowDuplicated(rowResultDto, termImportDto, syntaxValidList);
        assertTrue(rowResultDto.getValidRowExistedTermInDataBase().contains(termImportDto));
    }

    @Test
    void testIsRowDuplicated_shouldReturnTrue_whenInputNotExistedTermInDataBase() {
        RowResultDto rowResultDto = new RowResultDto();
        TermImportDto termImportDto = new TermImportDto(1, "dod", "definition of done");
        List<TermImportDto> syntaxValidList = new ArrayList<>();
        rowResultDto.getValidRowNotExistedTermInDataBase().add(termImportDto);
        termServiceImpl.isRowDuplicated(rowResultDto, termImportDto, syntaxValidList);
        assertTrue(rowResultDto.getValidRowNotExistedTermInDataBase().contains(termImportDto));
    }

    @Test
    void testIsRowDuplicated_shouldReturnTrue_whenDuplicateInSyntaxValidList() {
        RowResultDto rowResultDto = new RowResultDto();
        TermImportDto termImportDto = new TermImportDto(1, "dod", "definition of done");
        List<TermImportDto> syntaxValidList = new ArrayList<>();
        syntaxValidList.add(termImportDto);
        termServiceImpl.isRowDuplicated(rowResultDto, termImportDto, syntaxValidList);
        assertTrue(syntaxValidList.contains(termImportDto));
    }

    @Test
    void testIsRowDuplicated_shouldReturnFalse_whenNoDuplicateInDataBaseOrSyntaxValidList() {
        RowResultDto rowResultDto = new RowResultDto();
        TermImportDto termImportDto = new TermImportDto();
        List<TermImportDto> syntaxValidList = new ArrayList<>();
        assertFalse(termServiceImpl.isRowDuplicated(rowResultDto, termImportDto, syntaxValidList));
    }

    @Test
    void testGetStopCountingRow_shouldReturnLastCountedIndexRow_whenInputListOf2TermImport() {
        TermImportDto termImportDto1 = new TermImportDto(1, "DD", "dobadu");
        TermImportDto termImportDto2 = new TermImportDto(5, "TDD", "ToiDiDay");
        List<TermImportDto> rawDataList = new ArrayList<>();
        rawDataList.add(termImportDto1);
        rawDataList.add(termImportDto2);
        assertEquals(5, termServiceImpl.getStopCountingRow(rawDataList));
    }

    @Test
    void testGetStopCountingRow_shouldReturn1_whenInputEmptyList() {

        List<TermImportDto> rawDataList = new ArrayList<>();

        assertEquals(1, termServiceImpl.getStopCountingRow(rawDataList));
    }

    @Test
    void testSaveNewDescriptionWithExistedTerm_shouldReturnListWith1NewDescription_whenInputExistedTermWithDifferentDescription() {
        String descriptionRequest = "Definition of done";
        List<String> descriptionRequestList = new ArrayList<>();
        descriptionRequestList.add(descriptionRequest);
        assertEquals(1, termServiceImpl.saveNewDescriptionWithExistedTerm("Dod", descriptionRequestList).size());

    }

    @Test
    void testSaveNewTermNewDescription_shouldReturnNameOfNewTerm_whenInputNewTerm() {
        String descriptionRequest = "Something To Read";
        List<String> descriptionRequestList = new ArrayList<>();
        descriptionRequestList.add(descriptionRequest);
        assertEquals("STR", termServiceImpl.saveNewTermNewDescription("STR", descriptionRequestList).getName());
        assertEquals(1, termServiceImpl.saveNewTermNewDescription("MM", descriptionRequestList).getDescriptionEntityList().size());
    }

    @Test
    void testSaveAllListOfValidRows_shouldReturnListOf1ExistedTermWithNewDescription_whenInputExistedTermWithNewDescription() {
        RowResultDto rowResultDto = new RowResultDto();
        TermImportDto existedTerm = new TermImportDto(1, "Dod", "Definition of done");
        TermImportDto existedTerm2 = new TermImportDto(2, "Dod", "DoubleDz");
        TermImportDto nonExistedTerm = new TermImportDto(3, "rum", "dasd");
        rowResultDto.getValidRowExistedTermInDataBase().add(existedTerm);
        rowResultDto.getValidRowExistedTermInDataBase().add(existedTerm2);
        rowResultDto.getValidRowNotExistedTermInDataBase().add(nonExistedTerm);
        Map<String, Integer> returnMapFromSaveAll = termServiceImpl.saveAllListOfValidRows(rowResultDto);
        assertTrue(returnMapFromSaveAll.containsKey("Dod"));
        assertTrue(returnMapFromSaveAll.containsKey("rum"));
        assertEquals(2, returnMapFromSaveAll.get("Dod"));
        assertEquals(1, returnMapFromSaveAll.get("rum"));
    }

    @Test
    void testUploadTermAndDescriptionExcelFile_shouldReturnResponse_whenUploadSuccess() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_TERM_TEST_MULTIPLE_SCENARIO, "TERM_TEST_Multiple_Scenario.xlsx");
        ResponseForUploadFile returnFromUpload = termServiceImpl.uploadTermAndDescriptionExcelFile(multipartFile);
        System.out.println(returnFromUpload);

        assertEquals(10, returnFromUpload.getRowStopCounting());
        assertEquals(1, returnFromUpload.getFailedRows().getTermIsNull().size());
        assertEquals(1, returnFromUpload.getFailedRows().getDuplicatedDescriptionInFile().size());
        assertEquals(1, returnFromUpload.getFailedRows().getInvalidDescriptionLength().size());
        assertEquals(1, returnFromUpload.getFailedRows().getInvalidTermLength().size());
        assertEquals(2, returnFromUpload.getFailedRows().getExistedInTheDatabase().size());
        assertEquals(2, returnFromUpload.getSuccessfulCases().size());
    }

    @Test
    void get5TermNameLike_shouldReturn1TermName_whenSearchingForC() {
        assertEquals(1, termService.get5TermNameLike("c").size());
    }

    @Test
    void get5TermNameLike_shouldReturn0TermName_whenSearchingForH() {
        assertEquals(0, termService.get5TermNameLike("h").size());
    }

    @Test
    void isExistedTermNameInDatabase_shouldReturnTrue_whenSearchingForScrum() {
        assertTrue(termServiceImpl.isExistedTermNameInDatabase("scrum"));
    }

    @Test
    void isExistedTermNameInDatabase_shouldReturnTrue_whenSearchingForDod() {
        assertTrue(termServiceImpl.isExistedTermNameInDatabase("Dod"));
    }

    @Test
    void isExistedTermNameInDatabase_shouldReturnFalse_whenSearchingForS() {
        assertFalse(termServiceImpl.isExistedTermNameInDatabase("s"));
    }

    @Test
    void isExistedTermNameInDatabase_shouldReturnFalse_whenSearchingForD() {
        assertFalse(termServiceImpl.isExistedTermNameInDatabase("d"));
    }

    @Test
    void validateNewTermName_shouldThrowException_whenTermAlreadyExisted() {
        try {
            termService.validateNewTermName("Dod");
        } catch (SystemException e) {
            assertEquals(errorMessage.termExistedInDatabase, e.getMessage());
        }
    }

    @Test
    void testSaveTermRequestDod_shouldReturnListNewTopic_afterSave() {
        Term acceptanceTerm = Term.builder()
                .name("acceptance term")
                .topicIdList(new ArrayList<>(Arrays.asList(1, 2)))
                .build();
        termService.save(acceptanceTerm);
        assertEquals(2, termTopicRepository.findAll().size());
    }

    @Test
    void testSaveTermRequestDod_shouldThrowSystemException_afterSave() {
        Term acceptanceTerm = Term.builder()
                .name("Dod")
                .relatedTermIdList(new ArrayList<>(Arrays.asList(1, 2)))
                .descriptionList(List.of())
                .topicIdList(List.of())
                .build();
        try {
            termService.save(acceptanceTerm);
        } catch (SystemException exception) {
            assertEquals(errorMessage.termExistedInDatabase, exception.getMessage());
        }

    }

    @Test
    void testFind10RecentTerms_shouldReturnAListOf10RecentTerms_whenFound() {
        Term term3 = Term.builder()
                .name("Agile")
                .build();
        termService.save(term3);
        Term term4 = Term.builder()
                .name("Scrum master")
                .build();
        termService.save(term4);
        Term term5 = Term.builder()
                .name("Scrum meeting")
                .build();
        termService.save(term5);
        Term term6 = Term.builder()
                .name("Daily Scrum")
                .build();
        termService.save(term6);
        Term term7 = Term.builder()
                .name("IT Foundation")
                .build();
        termService.save(term7);
        Term term8 = Term.builder()
                .name("Agile discussion")
                .build();
        termService.save(term8);
        Term term9 = Term.builder()
                .name("Scrum object")
                .build();
        termService.save(term9);
        Term term10 = Term.builder()
                .name("Master")
                .build();
        termService.save(term10);




        Pageable top10Terms = PageRequest.of(0,10);
        assertEquals(10, termService.find10RecentTerms().size());
        assertEquals("Master", termService.find10RecentTerms().get(0).getName());
    }
    @Test
    void testGetTop10PopularTerms_shouldReturnAListOfPopularTeam_afterCall(){
        //Arrange
        UserEntity adminUser = new UserEntity();
        adminUser.setUsername("Hola");
        adminUser.setEmail("admin001@axonactive.com.vn");
        adminUser.setPassword("Aavn123!@#");
        userRepository.save(adminUser);

        TermEntity scrumMasterTerm = new TermEntity(1,"Scrum Master",null);
        TermEntity dodTerm = new TermEntity(2,"d.o.d",null);
        TermEntity legsOfScrumTerm = new TermEntity(3,"leg of scrum",null);

        List<DescriptionEntity> scrumMasterDescriptionList = new ArrayList<>();
        List<DescriptionEntity> dodDescriptionList = new ArrayList<>();
        List<DescriptionEntity> legsOfScrumDescriptionList = new ArrayList<>();

        DescriptionEntity descriptionWith8Vote = new DescriptionEntity(1,"The Scrum Master serves the Product Owner in several ways",LocalDate.now(),scrumMasterTerm,adminUser,8);
        DescriptionEntity descriptionWith4Vote = new DescriptionEntity(2,"an agreed upon list of the activities necessary to get a product increment to a done state by the end of a sprint",LocalDate.now(),dodTerm,adminUser,4);
        DescriptionEntity descriptionWith2Vote = new DescriptionEntity(3,"an agreed upon list of the activities necessary to get a product increment to a done state by the end of a sprint",LocalDate.now(),dodTerm,adminUser,2);

        scrumMasterDescriptionList.add(descriptionWith8Vote);
        dodDescriptionList.add(descriptionWith4Vote);
        dodDescriptionList.add(descriptionWith2Vote);
        legsOfScrumDescriptionList.add(descriptionWith2Vote);
        legsOfScrumDescriptionList.add(descriptionWith8Vote);

        scrumMasterTerm.setDescriptionEntityList(scrumMasterDescriptionList);
        dodTerm.setDescriptionEntityList(dodDescriptionList);
        legsOfScrumTerm.setDescriptionEntityList(legsOfScrumDescriptionList);

        termRepository.save(scrumMasterTerm);
        termRepository.save(dodTerm);
        termRepository.save(legsOfScrumTerm);

        //Action

        //Assert
        assertEquals(3,termService.getTop10PopularTerms().size());
    }

    @Test
    void testValidateListOfDescription_shouldReturnProperResult_whenGiveDodTermAndProperTermRequestAsVariables() {
        TermEntity dodTerm = new TermEntity(1, "Dod", new ArrayList<>());
        DescriptionEntity dodDescription = new DescriptionEntity(1, "This is Dod description", LocalDate.now(), dodTerm, new UserEntity(), 0);

        dodTerm.getDescriptionEntityList().add(dodDescription);

        Term newTermRequest = Term.builder()
                .descriptionList(new ArrayList<>())
                .build();
        Description newDescriptionRequest = new Description("This is Dod description", "Mquang102", 0);
        newTermRequest.getDescriptionList().add(newDescriptionRequest);

        Term anotherTermRequest = Term.builder()
                .descriptionList(new ArrayList<>())
                .build();
        Description anotherDescriptionRequest = new Description("This is Another Dod description", "Mquang102", 0);
        anotherTermRequest.getDescriptionList().add(anotherDescriptionRequest);

        try {
            termServiceImpl.validateListOfDescription(dodTerm, newTermRequest);
        } catch (SystemException e) {
            assertEquals(errorMessage.descriptionExistedInDatabase, e.getMessage());
        }

        assertEquals(termServiceImpl.validateListOfDescription(dodTerm, anotherTermRequest).get(0), anotherTermRequest.getDescriptionList().get(0));

    }
}

