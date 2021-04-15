package com.insulin.utils;

import com.insulin.exceptions.model.*;
import com.insulin.model.User;
import com.insulin.repository.AuthRepository;
import com.insulin.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.insulin.shared.constants.ExceptionConstants.*;
import static com.insulin.shared.constants.UserConstants.*;
import static java.util.Objects.isNull;

@Component
public class ValidationUtils {
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ValidationUtils(AuthRepository authRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validateCompleteInformation(String username, String email, String phoneNumber)
            throws UserNotFoundException, UsernameAlreadyExistentException,
            EmailAlreadyExistentException, PhoneNumberUniqueException {
        validateNewUsername(username);
        validateNewEmail(email);
        validatePhoneNumber(phoneNumber);
    }

    public void validatePhoneNumber(String phoneNumber) throws PhoneNumberUniqueException {
        if (StringUtils.isEmpty(phoneNumber)) {
            logger.error("Empty phone number!");
            throw new PhoneNumberUniqueException(INVALID_DATA);
        }
        int countUsers = userRepository.countUserByPhoneNr(phoneNumber);
        if (countUsers != 0) {
            logger.error("Phone number already used!");
            throw new PhoneNumberUniqueException(NOT_UNIQUE_PHONE);
        }
    }

    public void validateNewUsername(String username)
            throws UsernameAlreadyExistentException, UserNotFoundException {
        if (!StringUtils.isNotEmpty(username)) {
            logger.error("Empty username!");
            throw new UserNotFoundException(INVALID_DATA);
        }
        User userByUsername = authRepository.findUserByUsername(username);
        if (!isNull(userByUsername)) {
            logger.error("Username already existent!");
            throw new UsernameAlreadyExistentException(USERNAME_ALREADY_EXISTENT);
        }
    }

    public void validateNewEmail(String email)
            throws EmailAlreadyExistentException, UserNotFoundException {
        if (!StringUtils.isNotEmpty(email)) {
            logger.error("Empty email!");
            throw new UserNotFoundException(INVALID_DATA);
        }
        User userByEmail = authRepository.findUserByEmail(email);
        if (!isNull(userByEmail)) {
            logger.error("Email already existent!");
            throw new EmailAlreadyExistentException(EMAIL_ALREADY_EXISTENT);
        }
    }

    public static void validateNonNullUser(User user) throws UserNotFoundException {
        if (isNull(user)) {
            logger.error("The provided id is invalid. User not found");
            throw new UserNotFoundException("The provided id does not map to any user");
        }
    }

    public void validatePasswordNotIdentical(String encodedPassword, String password) throws OldPasswordException {
        if (checkSamePassword(password, encodedPassword)) {
            throw new OldPasswordException(OLD_PASSWORD);
        }
    }

    public void validateIdenticalPassword(String encodedPassword, String password) {
        if (!checkSamePassword(password, encodedPassword)) {
            throw new AccessDeniedException(NOT_ENOUGH_PERMISSION);
        }
    }

    public boolean checkSamePassword(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}
