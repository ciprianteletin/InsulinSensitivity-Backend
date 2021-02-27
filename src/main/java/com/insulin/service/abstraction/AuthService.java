package com.insulin.service.abstraction;

import com.insulin.exception.model.EmailAlreadyExistentException;
import com.insulin.exception.model.UserNotFoundException;
import com.insulin.exception.model.UsernameAlreadyExistentException;
import com.insulin.model.User;
import com.insulin.utils.model.CompleteUser;

import javax.mail.MessagingException;

public interface AuthService {

    void register(CompleteUser completeUser)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException, MessagingException;

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserByUsernameOrEmail(String text);

    User findUserById(Long id) throws UserNotFoundException;

    void resetPassword(User user);

    String redirectResetPassword(String email) throws MessagingException;
}
