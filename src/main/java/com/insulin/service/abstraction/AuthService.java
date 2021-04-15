package com.insulin.service.abstraction;

import com.insulin.exceptions.model.*;
import com.insulin.model.User;
import com.insulin.utils.model.CompleteUser;

import javax.mail.MessagingException;
import java.util.Optional;

public interface AuthService {

    void save(User user);

    void register(CompleteUser completeUser)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException, MessagingException, PhoneNumberUniqueException;

    Optional<User> findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserByUsernameOrEmail(String text);

    User findUserById(Long id) throws UserNotFoundException;

    void resetPassword(String password, String code) throws EmailNotFoundException, LinkExpiredException, OldPasswordException;

    void redirectResetPassword(String email) throws MessagingException, InvalidEmailForgotPassword;
}
