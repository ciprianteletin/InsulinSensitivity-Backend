package com.insulin.service.abstraction;

import com.insulin.exception.model.*;
import com.insulin.model.User;
import com.insulin.utils.model.CompleteUser;

import javax.mail.MessagingException;

public interface AuthService {

    void register(CompleteUser completeUser)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException, MessagingException, PhoneNumberUniqueException;

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserByUsernameOrEmail(String text);

    User findUserById(Long id) throws UserNotFoundException;

    void resetPassword(String password, String code) throws EmailNotFoundException, LinkExpiredException, OldPasswordException;

    void redirectResetPassword(String email) throws MessagingException, InvalidEmailForgotPassword;
}
