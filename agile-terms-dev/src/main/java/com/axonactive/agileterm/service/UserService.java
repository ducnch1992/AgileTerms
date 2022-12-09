package com.axonactive.agileterm.service;

import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.rest.client.model.User;
import com.axonactive.agileterm.rest.model.UserDto;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto findById(Integer id);

    UserDto save(User userRequest) throws MessagingException, UnsupportedEncodingException;

    void delete(Integer id);

    boolean isExistingEmail(String email);

    boolean isExistingUsername(String username);

    boolean passwordMatchesValid(String password, String matchingPassword);

//    void sendVerificationEmail(UserEntity userEntity, String siteURL) throws MessagingException, UnsupportedEncodingException;

    UserDto verifyUser(String verificationCode);

    void validateEmail(String email);

    void validateUserName(String username);

    UserEntity findUserEntityByUserName(String userName);
}
