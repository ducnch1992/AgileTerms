package com.axonactive.agileterm.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;


@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<String> handleConstraintViolation(final ConstraintViolationException e, final WebRequest webRequest) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);}

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> handleTopic(final ResourceNotFoundException ex, final WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<String> handleNullPointer(final NullPointerException e, final WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler({InputValidation.class})
    public ResponseEntity<String> handleInputValidationException( InputValidation e, final WebRequest webRequest) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<String> handleMaxSizeException(final MaxUploadSizeExceededException e, final WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(e.getMessage());
    }
    @ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<String> handleNumberFormatException(final NumberFormatException e, final WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler({SecurityException.class})
    public ResponseEntity<String> handleSecurityException(final SecurityException e, final WebRequest webRequest){
        log.info("unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler({MessagingException.class, UnsupportedEncodingException.class})
    public void handleEmailException(final MessagingException e1, final UnsupportedEncodingException e2, final WebRequest webRequest){
        log.info(e1.getMessage());
        log.info(e2.getMessage());
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<String> handleInvalidFormatException(final InvalidFormatException e, final WebRequest webRequest){
        log.info("File is not supported: " +e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler({AddressException.class})
    public ResponseEntity<String> handleInvalidEmailAddressException(final AddressException e, final WebRequest webRequest){
        log.info("Invalid email address: "+ e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler({MailSendException.class})
    public ResponseEntity<String> handleInvalidEmailAddressException(final MailSendException e, final WebRequest webRequest) {
        log.info("Invalid email address: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({SystemException.class})
    public ResponseEntity<String> handleSystemException(final SystemException e,final WebRequest webRequest){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
