package com.axonactive.agileterm.service.impl;

import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.exception.ErrorMessage;
import com.axonactive.agileterm.exception.InputValidation;
import com.axonactive.agileterm.exception.ResourceNotFoundException;
import com.axonactive.agileterm.repository.UserRepository;
import com.axonactive.agileterm.rest.client.model.Description;
import com.axonactive.agileterm.rest.client.model.Term;
import com.axonactive.agileterm.rest.client.model.TermTopic;
import com.axonactive.agileterm.rest.client.model.Topic;
import com.axonactive.agileterm.service.DescriptionService;
import com.axonactive.agileterm.service.TermService;
import com.axonactive.agileterm.service.TermTopicService;
import com.axonactive.agileterm.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("unit-test")
class TermTopicServiceImplTest {

    @Autowired
    private TopicService topicService;
    @Autowired
    private TermService termService;
    @Autowired
    private TermTopicService termTopicService;
    @Autowired
    private DescriptionService descriptionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TermTopicServiceImpl termTopicServiceImpl;

    @Autowired
    private ErrorMessage errorMessage;

    @BeforeEach
    void termTopicSetUp() {
        Term newTermDOD = Term.builder()
                .name("DOD")
                .build();
        termService.save(newTermDOD);
        Term newTerm1 = Term.builder()
                .name("Daily Scrum")
                .build();
        termService.save(newTerm1);

        topicService.save(new Topic("Agile", "#ffffff"));
        topicService.save(new Topic("Scrum", "#000000"));
        topicService.save(new Topic("Process", "#ffcb00"));


        termTopicService.save(new TermTopic(1, 1));
        termTopicService.save(new TermTopic(1, 2));
        termTopicService.save(new TermTopic(1, 3));

        //Author id 1
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
        //Vote id 1

        descriptionService.save(1, new Description("A set of conditions that software must meet in order to be accepted by a customer or stakeholder", "Huy", 0));

        descriptionService.save(1, new Description("A set of conditions that software must meet in order to be accepted by a customer or stakeholder", "Huy", 0));
    }

    @Test
    void testGetAll_shouldReturn3_WhenCheckSize() {
        assertEquals(3, termTopicService.getAll().size());
    }

    @Test
    void testFindById_shouldReturnAgile_whenCheckTopicNameOfFirstTermTopic() {
        assertEquals("Agile", termTopicService.findById(1).getTopicName());
    }

    @Test
    void testSave_shouldReturn4_whenAddNewTermTopicAndCheckSize() {
        termTopicService.save(new TermTopic(2, 1));
        assertEquals(4, termTopicService.getAll().size());
    }


    @Test
    void testFindTermByTopicId_shouldReturn1_WhenInput1AndCheckSize() {
        assertEquals(1, termTopicService.findTermByTopicId(1).size());
    }

    @Test
    void testFindTermByTopicName_shouldReturn1_whenInputAgileAndCheckSize() {
        assertEquals(1, termTopicService.findTermByTopicName("Agile").size());
    }

    @Test
    void testFindTopicByTermName_shouldReturn3_whenSearchForDoDAndCheckSize() {
        assertEquals(3, termTopicService.findTopicByTermName("DOD").size());
    }

    @Test
    void testFindTopicByTermId_shouldReturn1_whenSearchFor1AndCheckSize() {
        assertEquals(3, termTopicService.findTopicByTermId(1).size());
    }

    @Test
    void testFindTermDetailById_shouldReturnDoDAnd3_whenCheckNameAndCheckTopicSize() {
        assertEquals("DOD", termTopicService.findTermDetailById(Base64.getEncoder().encodeToString("DOD_1".getBytes())).getName());
        assertEquals(3, termTopicService.findTermDetailById(Base64.getEncoder().encodeToString("DOD_1".getBytes())).getTopicList().size());
    }
    @Test
    void testFindTermDetailById_shouldThrowException_whenInputLessThan2Characters() {
        try {
            termTopicService.findTermDetailById("a");
        } catch (ResourceNotFoundException e) {
            assertEquals(errorMessage.termNotFoundMsg, e.getMessage());
        }
    }
    @Test
    void testFindTermDetailById_shouldThrowException_whenIdIsNotFound(){
        try {
            termTopicService.findTermDetailById("QWNjZXB0YW5jZSBUZXN0XzI");
        } catch (ResourceNotFoundException e) {

            assertEquals(errorMessage.termTopicNotFoundMsg, e.getMessage());
        }
    }
    @Test
    void testFindById_shouldThrowException_whenIdIsNotFound(){
        try {
            termTopicService.findById(201);
        } catch (ResourceNotFoundException e) {
            assertEquals(errorMessage.termTopicNotFoundMsg, e.getMessage());
        }
    }
    @Test
    void testConvertTermTopicEntity_shouldThrowException_whenInputTermIsNotFound(){
        TermTopic termTopic = TermTopic.builder()
                .termId(100)
                .build();
        try {
        termTopicServiceImpl.convertTermTopicToTermTopicEntity(termTopic);
        }catch (ResourceNotFoundException e){
            assertEquals(errorMessage.termNotFoundMsg,e.getMessage());
        }
    }

    @Test
    void testConvertTermTopicEntity_shouldThrowException_whenInputTopicIsNotFound(){
        TermTopic termTopic = TermTopic.builder()
                .termId(1)
                .topicId(100)
                .build();
        try {
            termTopicServiceImpl.convertTermTopicToTermTopicEntity(termTopic);
        }catch (ResourceNotFoundException e){
            assertEquals(errorMessage.topicNotFoundMsg,e.getMessage());
        }
    }
    @Test
    void testSaveAll_shouldReturnListOfSavedTopic(){
        TermTopic termTopic = TermTopic.builder()
                .topicId(1)
                .termId(1)
                .build();
        assertEquals(1,termTopicServiceImpl.saveAll(Arrays.asList(termTopic)).size());

    }


}