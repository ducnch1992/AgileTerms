package com.axonactive.agileterm.service.impl;

import com.axonactive.agileterm.entity.Role;
import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.entity.UserRoleAssignmentEntity;
import com.axonactive.agileterm.entity.VerificationTokenEntity;
import com.axonactive.agileterm.exception.ErrorMessage;
import com.axonactive.agileterm.exception.ResourceNotFoundException;
import com.axonactive.agileterm.exception.SecurityException;
import com.axonactive.agileterm.repository.UserRepository;
import com.axonactive.agileterm.repository.VerificationTokenRepository;
import com.axonactive.agileterm.rest.client.model.User;
import com.axonactive.agileterm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("unit-test")
@Slf4j
class UserServiceImplTest {

    @Autowired
    private ErrorMessage errorMessage;

    @Mock
    JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User userRequest = new User(
            "zeus821999",
            "Hihi12345@",
            "Hihi12345@",
            "zeus821999@gmail.com"
    );

    private User userRequestWithInvalidPassword = new User(
            "zeus821999",
            "Hihi12345@",
            "Hihihi12345@",
            "zeus821999@gmail.com"
    );
    private User userRequestWithInvalidEmail = new User(
            "zeus821999",
            "Hihi12345@",
            "Hihihi12345@",
            "zeus821999@gmail"
    );
    private User userRequestWithInvalidPasswordFormat = new User(
            "zeus821999",
            "ihi12345",
            "ihihi12345",
            "zeus821999@gmail.com"
    );
    private User userRequestWithExistEmail = new User(
            "zeus821999",
            "Hihi12345@",
            "Hihihi12345@",
            "zeus821999@gmail.com"
    );
    private User userRequestWithInvalidUserName = new User(
            "zeus821999",
            "Hihi12345@",
            "Hihihi12345@",
            "zeus821999@gmail.com"
    );

    @BeforeEach
    void userSetUp() throws MessagingException, UnsupportedEncodingException {
        UserEntity userEntity1 = UserEntity.builder()
                .username("hotpanda")
                .email("hotpanda@mail.com")
                .password("p123456")
                .build();
        userRepository.save(userEntity1);
        UserEntity userEntity2 = UserEntity.builder()
                .username("hornysheep")
                .email("hornysheep@mail.com")
                .password("p654321")
                .build();
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity();
        verificationTokenRepository.save(verificationTokenEntity);
        UserEntity userEntity3 = UserEntity.builder()
                .username("hungthinhdn99")
                .email("hotpandahahaha@mail.com")
                .password("p123456")
                .verificationTokenEntity(verificationTokenEntity).build();
        userRepository.save(userEntity2);
        userRepository.save(userEntity3);
    }

    @Test
    void testGetAll_shouldReturnListOf2Users_whenFound() {

        assertEquals(3, userService.getAll().size());
    }

    @Test
    void testFindById_shouldReturn1User_whenFound() {
        assertEquals(1, userService.findById(1).getId());
    }

    @Test
    void testFindById_shouldReturnUsersName_whenFound() {
        assertEquals("hotpanda", userService.findById(1).getUsername());
    }

    @Test
    void testDelete_shouldReturn2_whenDelete1Users() {
        userService.delete(1);
        assertEquals(2, userService.getAll().size());
    }

    @Test
    void testDelete_shouldReturn1_whenDelete2Users() {
        userService.delete(1);
        userService.delete(2);
        assertEquals(1, userService.getAll().size());
    }

    @Test
    void testSave_shouldReturnUserId4AndTotalOf4UsersWithUserNameIsBigBlackBear_whenFound() throws MessagingException, UnsupportedEncodingException {
        User usersTest = new User();
        usersTest.setUserName("BigBlackBear");
        usersTest.setEmail("tongvu@gmail.com");
        usersTest.setPassword("Hihi12345@");
        usersTest.setMatchingPassword("Hihi12345@");
        assertEquals(4, userService.save(usersTest).getId());
        assertEquals(4, userService.getAll().size());
        assertEquals("bigblackbear", userService.findById(4).getUsername());
    }

    @Test
    void testCountUsersWithEmail_shouldReturn1_whenInputExistingEmail() {
        assertEquals(1, userRepository.countUsersWithEmail("hotpanda@mail.com"));
    }

    @Test
    void testCountUsersWithEmail_shouldReturn0_whenInputExistingEmail() {
        assertEquals(0, userRepository.countUsersWithEmail("mypanda@gmail.com"));
    }

    @Test
    void testCountUsersWithUsername_shouldReturn1_whenInputExistingUsername() {
        assertEquals(1, userRepository.countUsersWithUsername("hotpanda"));
    }

    @Test
    void testCountUsersWithUsername_shouldReturn0_whenInputExistingUsername() {
        assertEquals(0, userRepository.countUsersWithUsername("mypanda"));
    }

    @Test
    void testHasExistingEmail_shouldReturnTrue_whenInputExistingEmail() {
        assertTrue(userService.isExistingEmail("hotpanda@mail.com"));
    }

    @Test
    void testHasExistingEmail_shouldReturnFalse_whenInputNonExistingEmail() {
        assertFalse(userService.isExistingEmail("mypanda@mail.com"));
    }

    @Test
    void testHasExistingUsername_shouldReturnTrue_whenInputExistingUsername() {
        assertTrue(userService.isExistingUsername("hornysheep"));
    }

    @Test
    void testHasExistingUsername_shouldReturnFalse_whenInputNonExistingUsername() {
        assertFalse(userService.isExistingUsername("hornygoat"));
    }

    @Test
    void testPasswordMatching_shouldReturnTrue_whenCompareTwoPasswords(){
        assertTrue(userService.passwordMatchesValid(userRequest.getPassword(),userRequest.getMatchingPassword()));
    }

    @Test
    void testPasswordEncoder_shouldReturnTrue_whenCompareUserRequestPasswordWithUserPasswordFromDB() throws MessagingException, UnsupportedEncodingException {
        userService.save(userRequest);
        assertTrue(passwordEncoder.matches(userRequest.getPassword(),userRepository.findByUsername(userRequest.getUserName()).get().getPassword()));
    }

    @Test
    void testPasswordMatching_shouldThrowException_whenCompareTwoPasswords(){

        assertThrows(SecurityException.class, () -> userService.save(userRequestWithInvalidPassword));
    }

    @Test
    @Transactional
    void testGetRoleFromRequestUser_shouldReturnUserRole_FromDatabaseWhenCreate() throws MessagingException, UnsupportedEncodingException {
        userService.save(userRequest);
        UserRoleAssignmentEntity userRoleAssignmentEntity = userRepository.findByUsername(userRequest.getUserName()).get()
                .getRoles()
                .stream()
                .findFirst().get();
        Role role = userRoleAssignmentEntity.getRole();
        assertEquals(Role.ROLE_USER,role);
    }
    @Test
    void testInvalidPassword_shouldThrowException_whenSaveRequestUserWithInvalidPassword(){
        assertThrows(SecurityException.class, () -> userService.save(userRequestWithInvalidPasswordFormat));
    }
    @Test
    void testInvalidEmail_shouldThrowException_whenSaveRequestUserWithInvalidEmail(){
        assertThrows(SecurityException.class, () -> userService.save(userRequestWithInvalidEmail));
    }
    @Test
    void testExistedEmail_shouldThrowException_whenSaveRequestUserWithExistedEmail(){
        assertThrows(SecurityException.class, () -> userService.save(userRequestWithExistEmail));
    }
    @Test
    void testExistedUsername_shouldThrowException_whenSaveRequestUserWithExistedUsername(){
        assertThrows(SecurityException.class, () -> userService.save(userRequestWithInvalidUserName));
    }

    @Test
    void testFindUserByVerifyCode_shouldReturnUser_whenFound(){
        assertEquals("hungthinhdn99",userService.verifyUser(userRepository.findById(3).get().getVerificationTokenEntity().getVerificationCode()).getUsername());
    }

    @Test
    void testThrowExceptionWhenFindUserByVerifyCode_shouldThrowException_WhenNotFoundUser(){
        String randomCode = RandomString.make(64);
        assertThrows(ResourceNotFoundException.class, () -> userService.verifyUser(randomCode));
    }
    @Test
    void testThrowExceptionWhenFindUserByVerifyCode_ShouldThrowException_WhenAccountAlreadyActivated(){
        try{
            userService.verifyUser(userRepository.findById(3).get().getVerificationTokenEntity().getVerificationCode());
        }catch (SecurityException e){
            assertEquals(errorMessage.userAccountAlreadyActivated,e.getMessage());
        }
    }

    @Test
    void testFindUserByVerifyCode_shouldSetUserActiveStatusTrue_whenFound(){
        UserEntity verifyUserEntity = userRepository.findById(3).get();
        String verifyCode = verifyUserEntity.getVerificationTokenEntity().getVerificationCode();
        assertTrue(userService.verifyUser(verifyCode).getActivated());
    }

    @Test
    void testCheckUsernameExistInDatabase_shouldThrowException_whenUsernameIsExist(){
        assertThrows(SecurityException.class,() -> userService.validateUserName("hotpanda"));
    }

    @Test
    void testCheckEmailExistInDatabase_shouldThrowException_whenEmailIsExist(){
        assertThrows(SecurityException.class,() -> userService.validateEmail("hotpanda@mail.com"));
    }

    @Test
    void testFindById_shouldThrowException_whenUserIsNotFound(){
        try {
            userService.findById(1000);
        }
        catch (ResourceNotFoundException e){
            assertEquals(errorMessage.userNotFoundMsg,e.getMessage());
        }
    }

    @Test
    void testSave_shouldThrowExceptionWhenUserEmailExisted() throws MessagingException, UnsupportedEncodingException {
        User devUser = User.builder()
                .email("hotpanda@mail.com")
                .build();
        try {
            userService.save(devUser);
        }catch (SecurityException e){
            assertEquals(errorMessage.userEmailExisted,e.getMessage());
        }
    }

    @Test
    void testSave_shouldThrowExceptionWhenUserNameExisted() throws MessagingException, UnsupportedEncodingException {
        User devUser = User.builder()
                .email("hotpanda@mail231.com")
                .userName("hotpanda")
                .build();
        try {
            userService.save(devUser);
        } catch (SecurityException e) {
            assertEquals(errorMessage.userNameExisted, e.getMessage());
        }
    }

    @Test
    void testValidateEmail_shouldThrowException_whenEmailExited(){
        String email = "hotpanda@mail231.com";
        try{
            userService.validateEmail(email);
        }
        catch (SecurityException e){
            assertEquals(errorMessage.userEmailExisted,e.getMessage());
        }
    }

    @Test
    void testValidateUserName_shouldThrowException_whenUserIsExisted(){
        String userName = "hotpanda";
        try{
            userService.validateUserName(userName);
        }
        catch (SecurityException e){
            assertEquals(errorMessage.userNameExisted,e.getMessage());
        }
    }

}