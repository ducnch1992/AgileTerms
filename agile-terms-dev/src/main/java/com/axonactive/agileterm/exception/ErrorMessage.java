package com.axonactive.agileterm.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ErrorMessage {

    @Value("exception.system.session-timed-out")
    public String sessionTimedOut;
    @Value("${exception.resource-not-found-topic}")
    public String topicNotFoundMsg;

    @Value("${exception.resource-not-found-term}")
    public String termNotFoundMsg;

    @Value("${exception.resource-not-found-term-topic}")
    public String termTopicNotFoundMsg;

    @Value("${exception.resource-not-found-description}")
    public String descriptionNotFoundMsg;


    @Value("${exception.resource-not-found-vote}")
    public String voteNotFoundMsg;

    @Value("${exception.resource-not-found-user}")
    public String userNotFoundMsg;

    @Value("${exception.security.user-email-existed}")
    public String userEmailExisted;

    @Value("${exception.security.username-existed}")
    public String userNameExisted;

    @Value("${exception.security.confirm-password-not-match}")
    public String confirmPasswordNotMatch;

    @Value("${exception.security.user-email-invalid}")
    public String userEmailInvalid;

    @Value("${exception.security.password-invalid}")
    public String passwordInvalid;

    @Value("${exception.input.validation}")
    public String inputIdInvalid;

    @Value("${exception.system.account-already-activate}")
    public String userAccountAlreadyActivated;

    @Value("${exception.system.term-already-existed}")
    public String termExistedInDatabase;

    @Value("${exception.system.description-already-existed}")
    public String descriptionExistedInDatabase;

    @Value("${exception.system.description-must-not-null}")
    public String descriptionMustNotNull;


}
