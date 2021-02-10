package com.insulin.service.implementation;

import com.insulin.exception.model.EmailAlreadyExistentException;
import com.insulin.exception.model.UserNotFoundException;
import com.insulin.exception.model.UsernameAlreadyExistentException;
import com.insulin.model.User;
import com.insulin.model.UserDetail;
import com.insulin.model.UserPrincipal;
import com.insulin.repository.UserRepository;
import com.insulin.service.LoginAttemptService;
import com.insulin.service.UserService;
import com.insulin.utils.model.CompleteUser;
import com.insulin.utils.abstractions.AbstractUserFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

import static com.insulin.shared.UserConstants.*;
import static java.util.Objects.isNull;

@Service
@Transactional
@Qualifier("userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final AbstractUserFactory userFactory;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AbstractUserFactory userFactory, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.loginAttemptService = loginAttemptService;
    }

    /**
     * Method used by Spring Security, to fetch the current user from
     * the database. Throws an error if the user is not existent
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            logger.error(USER_NOT_FOUND + ": " + username);
            throw new UsernameNotFoundException(USER_NOT_FOUND + ": " + username);
        }
        logger.info("User found for username: " + username);
        UserPrincipal principal = new UserPrincipal(user);
        validateLoginAttempt(principal);
        user.getDetails()
                .setLastLoginDateDisplay(user.getDetails().getLastLoginDate()); //last login
        user.getDetails().setLastLoginDate(new Date()); // current login, now
        userRepository.save(user); //update information
        logger.info("Returning found user");
        return principal;
    }

    public void validateLoginAttempt(UserPrincipal userPrincipal) {
        logger.info("Validating the user authentication");
        if (userPrincipal.isNoActiveCaptcha()) {
            userPrincipal.setNoActiveCaptcha(!loginAttemptService.isExceededMaxAttempts(userPrincipal.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginCache(userPrincipal.getUsername());
        }
    }

    @Override
    public void register(CompleteUser completeUser) throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException {
        validateNewUsernameAndEmail(completeUser.getUsername(), completeUser.getEmail());
        logger.info("Username and e-mail ok");
        User user = userFactory.createUser(completeUser);
        UserDetail userDetail = userFactory.createUserDetails(completeUser);
        user.setDetails(userDetail);
        userDetail.setUser(user);
        userRepository.save(user);
        logger.info("User registered with success!");
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    private void validateNewUsernameAndEmail(String username, String email) throws UserNotFoundException, UsernameAlreadyExistentException, EmailAlreadyExistentException {
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
}
