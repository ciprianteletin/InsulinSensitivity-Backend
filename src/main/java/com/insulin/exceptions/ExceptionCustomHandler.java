package com.insulin.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.insulin.exceptions.model.*;
import com.insulin.shared.HttpResponse;
import com.insulin.utils.model.CaptchaModel;
import com.itextpdf.text.DocumentException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;

import static com.insulin.shared.constants.ExceptionConstants.*;
import static com.insulin.shared.constants.SecurityConstants.CAPTCHA_HEADER;
import static com.insulin.utils.HttpResponseUtils.buildHttpResponseEntity;
import static com.insulin.utils.HttpResponseUtils.buildHttpResponseWithHeader;

/**
 * Special class which has the role to 'handle' all the exceptions and to return a specific, easy to understand message to the user.
 * It covers both custom exception and exception already existent. Also, it treats unexpected exceptions by creating a handler for Exception class.
 * It's using the class HttpResponse to follow the convention used for other errors generated by authentication.
 * The logger is used for debugging purpose.
 */
@RestControllerAdvice
public class ExceptionCustomHandler {
    private final Logger logger = LoggerFactory.getLogger(ExceptionCustomHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse> invalidField(MethodArgumentNotValidException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, INVALID_FIELD);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDenied(AccessDeniedException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(OldPasswordException.class)
    public ResponseEntity<HttpResponse> oldPasswordException(OldPasswordException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, OLD_PASSWORD);
    }

    @ExceptionHandler(EmailAlreadyExistentException.class)
    public ResponseEntity<HttpResponse> emailExistentException(EmailAlreadyExistentException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistentException.class)
    public ResponseEntity<HttpResponse> usernameExistentException(UsernameAlreadyExistentException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HttpResponse> usernameNotFoundException(UsernameNotFoundException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(PhoneNumberUniqueException.class)
    public ResponseEntity<HttpResponse> uniquePhoneNumber(PhoneNumberUniqueException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidEmailForgotPassword.class)
    public ResponseEntity<HttpResponse> invalidEmailForgotPass(InvalidEmailForgotPassword exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
    }

    @ExceptionHandler(InputIndexException.class)
    public ResponseEntity<HttpResponse> inputIndex(InputIndexException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidHistoryId.class)
    public ResponseEntity<HttpResponse> invalidHistoryId(InvalidHistoryId exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ActivateCaptchaException.class)
    public ResponseEntity<CaptchaModel> activateCaptchaCode() {
        logger.info("Activate cache for the current user!");
        return new ResponseEntity<>(new CaptchaModel(true), HttpStatus.OK);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<HttpResponse> invalidDataException(InvalidDataException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<HttpResponse> dataFormatException() {
        logger.error(IMAGE_EXCEPTION);
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, IMAGE_EXCEPTION);
    }

    @ExceptionHandler(UpdateDeniedException.class)
    public ResponseEntity<HttpResponse> deniedUpdateException(InvalidDataException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(LinkExpiredException.class)
    public ResponseEntity<HttpResponse> linkExpiredException(LinkExpiredException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HttpResponse> constraintException(ConstraintViolationException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException() {
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return buildHttpResponseEntity(HttpStatus.REQUEST_TIMEOUT, exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException() {
        return buildHttpResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<HttpResponse> noSuchElementException() {
        return buildHttpResponseEntity(HttpStatus.SERVICE_UNAVAILABLE, NO_ELEMENT);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
        return buildHttpResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> fileException() {
        return buildHttpResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, FILE_ERROR);
    }

    @ExceptionHandler(DocumentException.class)
    public ResponseEntity<HttpResponse> documentException() {
        return buildHttpResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, PDF_ERROR);
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public ResponseEntity<HttpResponse> undeclaredException(UndeclaredThrowableException exception) {
        if (exception.getUndeclaredThrowable() instanceof ActivateCaptchaException) {
            HttpHeaders errorHeader = new HttpHeaders();
            errorHeader.set(CAPTCHA_HEADER, "activate");
            return buildHttpResponseWithHeader(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS, errorHeader);
        }
        return buildHttpResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> unexpectedException(Exception exception) {
        logger.error(exception.getMessage());
        exception.printStackTrace();
        return buildHttpResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
    }
}
