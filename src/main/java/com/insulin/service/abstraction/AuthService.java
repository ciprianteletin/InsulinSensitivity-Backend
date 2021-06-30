package com.insulin.service.abstraction;

import com.insulin.exceptions.model.*;
import com.insulin.model.User;
import com.insulin.utils.model.CompleteUser;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface AuthService {

    void save(User user);

    void register(CompleteUser completeUser)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException, MessagingException, PhoneNumberUniqueException;

    void resetPassword(String password, String code) throws EmailNotFoundException, LinkExpiredException, OldPasswordException;

    void redirectResetPassword(String email) throws MessagingException, InvalidEmailForgotPassword;

    int checkPassword(String password) throws URISyntaxException, IOException, InterruptedException;
}
