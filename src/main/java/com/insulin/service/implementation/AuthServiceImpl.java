package com.insulin.service.implementation;

import com.insulin.exception.model.*;
import com.insulin.model.User;
import com.insulin.model.UserDetail;
import com.insulin.model.UserPrincipal;
import com.insulin.repository.AuthRepository;
import com.insulin.service.EmailService;
import com.insulin.service.LoginAttemptService;
import com.insulin.service.abstraction.AuthService;
import com.insulin.utils.model.CompleteUser;
import com.insulin.utils.abstractions.AbstractUserFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDate;

import static com.insulin.shared.UserConstants.*;
import static com.insulin.utils.AuthenticationUtils.checkIfEmail;
import static com.insulin.utils.AuthenticationUtils.encryptPassword;
import static java.util.Objects.isNull;

@Service
@Transactional
@Qualifier("userService")
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AuthRepository authRepository;
    private final AbstractUserFactory userFactory;
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository, AbstractUserFactory userFactory,
                           LoginAttemptService loginAttemptService, EmailService emailService) {
        this.authRepository = authRepository;
        this.userFactory = userFactory;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
    }

    /**
     * Method used by Spring Security, to fetch the current user from
     * the database. Throws an error if the user is not existent. The user can be found based on his username/email
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsernameOrEmail(username);
        logger.info("User found for username: " + username);
        UserPrincipal principal = new UserPrincipal(user);
        validateLoginAttempt(principal);
        user.getDetails()
                .setLastLoginDateDisplay(user.getDetails().getLastLoginDate()); //last login
        user.getDetails().setLastLoginDate(LocalDate.now()); // current login, now
        authRepository.save(user); //update information
        logger.info("Returning found user");
        return principal;
    }

    @Override
    public User findUserByUsernameOrEmail(String text) {
        User user;
        if (checkIfEmail(text)) {
            user = authRepository.findUserByEmail(text);
            if (isNull(user)) {
                logger.error(USER_NOT_FOUND + ": " + text);
                throw new UsernameNotFoundException(USER_NOT_FOUND + " for email address " + text);
            }
            return user;
        }
        user = authRepository.findUserByUsername(text);
        if (user == null) {
            logger.error(USER_NOT_FOUND + ": " + text);
            throw new UsernameNotFoundException(USER_NOT_FOUND + ": " + text);
        }
        return user;
    }

    @Override
    public User findUserById(Long id) throws UserNotFoundException {
        return authRepository.findById(id) //
                .orElseThrow(() -> new UserNotFoundException("User not found for the specified id. Auto-login failed"));
    }

    @Override
    public void resetPassword(User user) throws EmailNotFoundException {
        User forgottenUser = this.findUserByEmail(user.getUsername());
        if (forgottenUser == null) {
            throw new EmailNotFoundException("The provided email was not mapped to any user!");
        }
        forgottenUser.setPassword(encryptPassword(user.getPassword()));
        authRepository.save(forgottenUser);
    }

    /**
     * The secretParam usage is that it will be send as a httpOnly cookie, hence available only on the server.
     * If the secret received from the client and the value from the cookie are not matching, the user will
     * not be able to reset his password.
     */
    @Override
    public String redirectResetPassword(String email) throws MessagingException, InvalidEmailForgotPassword {
        User existUser = authRepository.findUserByEmail(email);
        if (existUser == null) {
            throw new InvalidEmailForgotPassword("The provided email does not exist!");
        }
        String secretParam = RandomStringUtils.randomAlphanumeric(15);
        emailService.sendResetPasswordEmail(email, secretParam);
        return secretParam;
    }

    @Override
    public void register(CompleteUser completeUser) throws UserNotFoundException, EmailAlreadyExistentException,
            UsernameAlreadyExistentException, MessagingException {
        validateNewUsernameAndEmail(completeUser.getUsername(), completeUser.getEmail());
        logger.info("Username and e-mail ok");
        User user = userFactory.createUser(completeUser);
        UserDetail userDetail = userFactory.createUserDetails(completeUser);
        user.setBidirectionalDetails(userDetail);
        authRepository.save(user);
        emailService.sendRegisterEmail(userDetail.getFirstName(), userDetail.getEmail());
        logger.info("User registered with success!");
    }

    @Override
    public User findUserByUsername(String username) {
        return authRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return authRepository.findUserByEmail(email);
    }

    private void validateNewUsernameAndEmail(String username, String email)
            throws UserNotFoundException, UsernameAlreadyExistentException, EmailAlreadyExistentException {
        if (!StringUtils.isNotEmpty(username) || !StringUtils.isNotEmpty(email)) {
            logger.error("Empty username or email!");
            throw new UserNotFoundException(INVALID_DATA);
        }
        User userByUsername = findUserByUsername(username);
        if (!isNull(userByUsername)) {
            logger.error("Username already existent!");
            throw new UsernameAlreadyExistentException(USERNAME_ALREADY_EXISTENT);
        }
        User userByEmail = findUserByEmail(email);
        if (!isNull(userByEmail)) {
            logger.error("Email already existent!");
            throw new EmailAlreadyExistentException(EMAIL_ALREADY_EXISTENT);
        }
    }

    private void validateLoginAttempt(UserPrincipal userPrincipal) {
        logger.info("Validating the user authentication");
        if (userPrincipal.isNoActiveCaptcha()) {
            userPrincipal.setNoActiveCaptcha(!loginAttemptService.isExceededMaxAttempts(userPrincipal.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginCache(userPrincipal.getUsername());
        }
    }
}
