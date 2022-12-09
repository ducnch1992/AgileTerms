package com.axonactive.agileterm.utility;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("unit-test")
@RequiredArgsConstructor
class EmailPasswordUtilsTest {

    @Test
    void testIsEmailValid_shouldReturnTrue_whenEnterValidEmail() {
        String email = "user1@gmail.com";
        assertTrue(EmailPasswordUtils.isEmailValid(email));
    }

    @Test
    void testIsEmailValid_shouldReturnTrue_whenEnterValidEmailWithDot() {
        String email = "user1@gmail.com.vn";
        assertTrue(EmailPasswordUtils.isEmailValid(email));
    }

    @Test
    void testIsEmailValid_shouldReturnFalse_whenEnterInvalidEmail() {
        String email = "user1@gmailcom";
        assertFalse(EmailPasswordUtils.isEmailValid(email));
    }



    @ParameterizedTest
    @ValueSource(strings = {"aA123", "1Aa456 7890", ""})
    void testIsPasswordValid_shouldReturnFalse_whenEnterInvalidPassword(String password){
        assertFalse(EmailPasswordUtils.isPasswordValid(password));
    }
    @Test
    void testIsPasswordValid_shouldReturnTrue_whenEnterPasswordWithNumberUppercaseLowercaseAnd10Character() {
        String password = "1Aa4567890";
        assertTrue(EmailPasswordUtils.isPasswordValid(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1a@4565789", "1A#@456790", "aA%defKghj"})
    void testIsPasswordValid_shouldReturnTrue_whenEnterPasswordThatFollowFormat(String password) {
        assertTrue(EmailPasswordUtils.isPasswordValid(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1a34567890", "1A34567890", "aAcdefghij", "A@CDEFGHIJ", "1@34567890"})
    void testIsPasswordValid_shouldReturnFalse_whenEnterPasswordThatDontFollowFormat(String password) {
        assertFalse(EmailPasswordUtils.isPasswordValid(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "abcdefghijk", "ABCDEFgHIJ", "!@#$%^&*()_"})
    void testIsPasswordValid_shouldReturnFalse_whenEnterPasswordOnlyMeetOneCondition(String password) {
        assertFalse(EmailPasswordUtils.isPasswordValid(password));
    }



}
