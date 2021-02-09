package com.insulin.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.insulin.exception.model.*;
import com.insulin.shared.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;

import java.io.IOException;

import static com.insulin.shared.ExceptionConstants.*;
import static com.insulin.utils.HttpResponseUtils.buildHttpResponseEntity;

/**
 * Special class which has the role to 'handle' all the exceptions and to return a specific, easy to understand message to the user.
 * It covers both custom exception and exception already existent. Also, it treats unexpected exceptions by creating a handler for Exception class.
 * It's using the class HttpResponse to follow the convention used for other errors generated by authentication.
 * The logger is used for debugging purpose.
 */
//TODO extend when needed
@RestControllerAdvice
public class ExceptionCustomHandler {
    private final Logger logger = LoggerFactory.getLogger(ExceptionCustomHandler.class);

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

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<HttpResponse> invalidDataException(InvalidDataException exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException() {
        return buildHttpResponseEntity(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return buildHttpResponseEntity(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return buildHttpResponseEntity(HttpStatus.GATEWAY_TIMEOUT, exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException() {
        return buildHttpResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
        return buildHttpResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> fileException() {
        return buildHttpResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, FILE_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> unexpectedException(Exception exception) {
        logger.error(exception.getMessage());
        return buildHttpResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
    }
}
