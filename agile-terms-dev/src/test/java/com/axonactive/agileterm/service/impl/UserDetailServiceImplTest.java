package com.axonactive.agileterm.service.impl;

import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.entity.VerificationTokenEntity;
import com.axonactive.agileterm.exception.ResourceNotFoundException;
import com.axonactive.agileterm.repository.UserRepository;
import com.axonactive.agileterm.repository.VerificationTokenRepository;
import com.axonactive.agileterm.service.security.impl.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("unit-test")
@Slf4j
class UserDetailServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserDetailServiceImpl userDetailService;

        UserEntity userEntity1 = UserEntity.builder()
                .username("hotpanda")
                .email("hotpanda@mail.com")
                .password("p123456")
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .username("hornysheep")
                .email("hornysheep@mail.com")
                .password("p654321")
                .build();
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity();

        UserEntity userEntity3 = UserEntity.builder()
                .username("hungthinhdn99")
                .email("hotpandahahaha@mail.com")
                .password("p123456")
                .activated(true)
                .verificationTokenEntity(verificationTokenEntity).build();
        @BeforeEach
    void userSetUp() throws MessagingException, UnsupportedEncodingException {
            verificationTokenRepository.save(verificationTokenEntity);
            userRepository.save(userEntity1);
            userRepository.save(userEntity2);
            userRepository.saveAndFlush(userEntity3);
        }

    @Test
    void testLoadUserByUsername_shouldReturnUser_whenFound() {
        assertEquals(userEntity3.getUsername(),userDetailService.loadUserByUsername("hungthinhdn99").getUsername());
    }

    @Test
    void testLoadUserByName_shouldThrowException_whenNotFoundUser(){
            assertThrows(ResourceNotFoundException.class,() -> userDetailService.loadUserByUsername("hihi"));
    }

    @Test
    void testBuildUserDetailsByUserName_shouldReturnUser_whenFound(){
            assertEquals(userEntity3.getPassword(),userDetailService.loadUserByUsername(userEntity3.getUsername()).getPassword());

            assertEquals(0,userDetailService.loadUserByUsername(userEntity3.getUsername()).getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()).size());
    }


}