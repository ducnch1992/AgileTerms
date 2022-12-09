package com.axonactive.agileterm.service.impl;

import com.axonactive.agileterm.exception.ErrorMessage;
import com.axonactive.agileterm.exception.ResourceNotFoundException;
import com.axonactive.agileterm.rest.client.model.Term;
import com.axonactive.agileterm.rest.client.model.TermTopic;
import com.axonactive.agileterm.rest.client.model.Topic;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("unit-test")
class TopicServiceImplTest {
    @Autowired
    private ErrorMessage errorMessage;
    @Autowired
    TopicService topicService;

    @Autowired
    TermService termService;

    @Autowired
    TermTopicService termTopicService;

    @BeforeEach
    void setup() {
        Topic scrum = Topic.builder()
                .name("Scrum")
                .color("#FF5733")
                .build();
        Topic process = Topic.builder()
                .name("Process")
                .color("#33FF33")
                .build();
        Topic agile = Topic.builder()
                .name("Agile")
                .color("#0000FF")
                .build();
        topicService.save(scrum);
        topicService.save(process);
        topicService.save(agile);
        Term newTerm = Term.builder()
                .name("DOD").build();
        Term newerTerm = Term.builder()
                        .name("Backlog").build();
        termService.save(newTerm);
        termService.save(newerTerm);
        termTopicService.save(new TermTopic(1, 1));
        termTopicService.save(new TermTopic(1, 2));
        termTopicService.save(new TermTopic(2,1));
    }

    @Test
    void testGetAll_shouldReturn3_whenCheckSizeAfterCalled() {
        assertEquals(3, topicService.getAll().size());
    }

    @Test
    void testFindById_shouldReturnAgile_whenInputIdBy3AndCheckName() {
        assertEquals("Agile", topicService.findById(3).getName());
    }

    @Test
    void testAdd_shouldReturn4AndBacklog_whenAddNewTopicAndCheckSizeAndCheckName() {
        Topic backlog = Topic.builder()
                .name("Back Log")
                .color("#0000FF")
                .build();
        assertEquals(4, topicService.save(backlog).getId());
        assertEquals(4, topicService.getAll().size());
        assertEquals("Back Log", topicService.findById(4).getName());
    }

    @Test
    void testUpdate_shouldNameOfTopic2ToAgile_whenUpdateTopic2WithTopic3Info() {
        Topic agile = Topic.builder()
                .name("Agile")
                .color("#0000FF")
                .build();
        assertEquals("Agile", topicService.update(3, agile).getName());
    }

    @Test
    void testDelete_shouldReturn2_whenDeleteTheLastOneAndCheckSize() {
        topicService.delete(3);
        assertEquals(2, topicService.getAll().size());

    }

    @Test
    void testFindById_shouldThrowResourceNotFound_WhenInputAnNonExistentId() {
        try {
            topicService.findById(100);
        } catch (ResourceNotFoundException exception) {
            assertEquals(errorMessage.topicNotFoundMsg, exception.getMessage());
        }
    }

    @Test
    void testUpdateById_shouldThrowResourceNotFoundException_whenIdIsNotFound(){
        try {
            topicService.update(100,new Topic());
        }catch (ResourceNotFoundException e){
            assertEquals(errorMessage.topicNotFoundMsg,e.getMessage());
        }
    }

    @Test
    void testGetPopularTopic_shouldReturnTopicScrum_whenScrumHasBeenRelatedToMoreTerm() {
       assertEquals("Scrum", topicService.getPopularTopic().get(0).getName());
    }
}