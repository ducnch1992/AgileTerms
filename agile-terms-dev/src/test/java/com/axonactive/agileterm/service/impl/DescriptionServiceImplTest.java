package com.axonactive.agileterm.service.impl;

import com.axonactive.agileterm.entity.Role;
import com.axonactive.agileterm.entity.TermEntity;
import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.entity.UserRoleAssignmentEntity;
import com.axonactive.agileterm.exception.ErrorMessage;
import com.axonactive.agileterm.exception.ResourceNotFoundException;
import com.axonactive.agileterm.repository.TermRepository;
import com.axonactive.agileterm.repository.UserRepository;
import com.axonactive.agileterm.rest.client.model.Description;
import com.axonactive.agileterm.rest.client.model.Term;
import com.axonactive.agileterm.rest.model.DescriptionDto;
import com.axonactive.agileterm.service.DescriptionService;
import com.axonactive.agileterm.service.TermService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("unit-test")
class DescriptionServiceImplTest {


    @Autowired
    private TermService termService;

    @Autowired
    private DescriptionService descriptionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private ErrorMessage errorMessage;

    @BeforeEach
    void descriptionSetUp() {

        Term term = new Term();
        term.setName("DOD");
        termService.save(term);
        Term term2 = new Term();
        term2.setName("CODE");
        termService.save(term2);



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

        descriptionService.save(1, new Description("A set of conditions that software must meet in order to be accepted by a customer or stakeholder", "Huy", 0));

        descriptionService.save(1, new Description("A set of conditions that software must meet in order to be accepted by an customer or stakeholder", "Huy", 0));


    }

    @Test
    void testGetAll_shouldReturnListOf2Descriptions_whenFound() {
        assertEquals(2, descriptionService.getAll().size());
    }

    @Test
    void testFindById_shouldReturnDescriptionOfTermDOD_whenInput1() {
        assertEquals(1, descriptionService.findById(1).getId());
    }

    @Test
    void testSave_shouldReturnDescriptionOfAuthorCODE_whenSaveANewDescription() {
        DescriptionDto createdDescription = descriptionService.save(2, new Description("The description of the objective criteria the team will use to determine whether or not a Story achieves the Value it represents", "Quang", 0));
        assertEquals(3, createdDescription.getId());
    }

    @Test
    void testDelete_shouldReturn0_whenDeleteExistedDescription() {
        int descriptionSize = descriptionService.getAll().size();
        descriptionService.deleteById(1);
        assertEquals(descriptionSize - 1, descriptionService.getAll().size());
    }

    @Test
    void testUpdate_shouldReturnNewDescription_whenUpdatedExistedDescription() {
        Description description = new Description("shouldThrowResourseNotFoundException", "Quang", 0);
        descriptionService.updateById(1, description, 1);
        assertEquals("Quang", descriptionService.findById(1).getAuthorName());
    }

    @Test
    void testFindById_shouldThrowResourseNotFoundException_whenInputNonExistedId() {
        try {
            descriptionService.findById(-1);
        } catch (ResourceNotFoundException e) {
            assertEquals(errorMessage.descriptionNotFoundMsg, e.getMessage());
        }

    }


    @Test
    void findByTermIdOrderByVotePointAndCreateDate_shouldReturnListOf2_WhenInputId1() {
        assertEquals(2, descriptionService.findByTermIdOrderByVotePointDescCreateDateDesc(1).size());
    }

    @Test
    void testFindDescriptionContentByTermName_shouldReturnListOf2_WhenInputNameOfDOD() {
        assertEquals(2, descriptionService.findDescriptionContentByTermName("DOD").size());
    }

    @Test
    void convertFromDescriptionContentListToDescriptionList() {
        UserEntity adminAuthorEntity = new UserEntity();
        adminAuthorEntity.setId(1);
        adminAuthorEntity.setUsername("admin");
        adminAuthorEntity.setEmail("admin@axonactive.com.vn");
        adminAuthorEntity.setPassword("Aavn123!@#");
        adminAuthorEntity.setActivated(true);
        UserRoleAssignmentEntity adminRole = new UserRoleAssignmentEntity(1, Role.ROLE_ADMIN,adminAuthorEntity);
        List<UserRoleAssignmentEntity> userRoleAssignmentEntityList = new ArrayList<>();
        userRoleAssignmentEntityList.add(adminRole);
        adminAuthorEntity.setRoles(userRoleAssignmentEntityList);
        userRepository.save(adminAuthorEntity);

//

        String termName = "OCD";
        TermEntity ocdTerm = new TermEntity(null, termName, null);
        termRepository.save(ocdTerm);

        Set<String> ocdDescriptionContentList = new HashSet<>();
        ocdDescriptionContentList.add("Fake definition of done 1");
        ocdDescriptionContentList.add("Criteria of check list");

        assertEquals(2,
                descriptionService.convertDescriptionContentListToDescriptionList(
                        ocdDescriptionContentList, ocdTerm, adminAuthorEntity).size()
        );
    }

    @Test
    void testUpdateDescriptionById_ShouldThrowException_whenDescriptionIsNotFound() {
        try{
            descriptionService.updateById(1,new Description(),10);
        }catch (ResourceNotFoundException e)
        {
            assertEquals(errorMessage.descriptionNotFoundMsg,e.getMessage());
        }
    }
    @Test
    void testUpdateDescriptionById_ShouldThrowException_whenTermIsNotFound() {
        try{
            descriptionService.updateById(100, new Description("Definition of Done","Huy",10),1);
        }
        catch (ResourceNotFoundException e)
        {
            assertEquals(errorMessage.termNotFoundMsg,e.getMessage());
        }
    }
    @Test
    void testUpdateDescriptionById_ShouldThrowException_whenAuthorIsNotFound() {
        try{
            descriptionService.updateById(1,new Description("Definition of Done","user",10),1);
        }catch (ResourceNotFoundException e)
        {
            assertEquals(errorMessage.userNotFoundMsg,e.getMessage());
        }
    }

    @Test
    void testSave_shouldThrowResourcesNotFoundException_whenTermIdIsNotFound(){

        try {
            descriptionService.save(100,new Description());
        }catch (ResourceNotFoundException e){
            assertEquals(errorMessage.termNotFoundMsg,e.getMessage());
        }
    }

    @Test
    void testSave_shouldThrowResourcesNotFoundException_whenUserIsNotFound(){
        Description description = Description.builder()
                .userName("Nhan Truong")
                .build();
        try {
            descriptionService.save(1,description );
        }catch (ResourceNotFoundException e){
            assertEquals(errorMessage.userNotFoundMsg,e.getMessage());
        }
    }

    @Test
    void testFindDescriptionByTermIdAndDescriptionString_shouldReturnAListOf0(){
        assertEquals(0,descriptionService.findDescriptionByTermIdAndDescriptionString("ALoha","saying hello in spanish").size());
    }



}