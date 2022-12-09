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
import com.axonactive.agileterm.rest.model.UserDto;
import com.axonactive.agileterm.service.UserService;
import com.axonactive.agileterm.service.mapper.UserMapper;
import com.axonactive.agileterm.utility.EmailSenderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.axonactive.agileterm.utility.EmailPasswordUtils.isEmailValid;
import static com.axonactive.agileterm.utility.EmailPasswordUtils.isPasswordValid;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ErrorMessage errorMessage;

    @Autowired
    private EmailSenderUtils emailSenderUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getAll() {
        return userMapper.toDtos(userRepository.findAll());
    }

    @Override

    public UserDto findById(Integer id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(errorMessage.userNotFoundMsg)));
    }

    @Override
    public UserDto save(User userRequest) throws MessagingException, UnsupportedEncodingException {

        if (isExistingEmail(userRequest.getEmail())) {
            throw new SecurityException(errorMessage.userEmailExisted);
        }

        if (isExistingUsername(userRequest.getUserName().replaceAll("\\s+", ""))) {
            throw new SecurityException(errorMessage.userNameExisted);
        }

        if (Boolean.FALSE.equals(isEmailValid(userRequest.getEmail()))) {
            throw new SecurityException(errorMessage.userEmailInvalid);
        }

        if (Boolean.FALSE.equals(isPasswordValid(userRequest.getPassword()))) {
            throw new SecurityException(errorMessage.passwordInvalid);
        }

        if (!(passwordMatchesValid(userRequest.getPassword(), userRequest.getMatchingPassword()))) {
            throw new SecurityException(errorMessage.passwordInvalid);
        }

        UserEntity createUserEntity = new UserEntity();
        List<UserRoleAssignmentEntity> userRoleAssignmentEntities = new ArrayList<>();
        userRoleAssignmentEntities.add(new UserRoleAssignmentEntity(null, Role.ROLE_USER, createUserEntity));
        createUserEntity.setRoles(userRoleAssignmentEntities);
        createUserEntity.setUsername(userRequest.getUserName().toLowerCase().replaceAll("\\s+", ""));
        createUserEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        createUserEntity.setEmail(userRequest.getEmail().toLowerCase());
        createUserEntity.setVerificationTokenEntity(verificationTokenRepository.save(new VerificationTokenEntity()));
        emailSenderUtils.emailSender(createUserEntity);
        return userMapper.toDto(userRepository.save(createUserEntity));
    }


    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean isExistingEmail(String email) {
        return userRepository.countUsersWithEmail(email.toLowerCase()) != 0;
    }

    @Override
    public boolean isExistingUsername(String username) {
        return userRepository.countUsersWithUsername(username.toLowerCase()) != 0;
    }

    @Override
    public boolean passwordMatchesValid(String password, String matchingPassword) {
        return password.equals(matchingPassword);
    }

    @Override
    public UserDto verifyUser(String verificationCode) {
        UserEntity verifyUserEntity = userRepository.findByVerificationTokenEntityVerificationCode(verificationCode)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage.userNotFoundMsg));
        if (Boolean.TRUE.equals(verifyUserEntity.getActivated()))
            throw new SecurityException(errorMessage.userAccountAlreadyActivated);
        else
            verifyUserEntity.setActivated(true);
        return userMapper.toDto(userRepository.save(verifyUserEntity));
    }

    @Override
    public void validateEmail(String email) {
        if (isExistingEmail(email)) throw new SecurityException(errorMessage.userEmailExisted);
    }

    @Override
    public void validateUserName(String username) {
        if (isExistingUsername(username.replaceAll("\\s+", "")))
            throw new SecurityException(errorMessage.userNameExisted);
    }

    @Override
    public UserEntity findUserEntityByUserName(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() -> new ResourceNotFoundException(errorMessage.userNotFoundMsg));
    }
}
