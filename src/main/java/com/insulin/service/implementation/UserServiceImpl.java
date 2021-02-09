package com.insulin.service.implementation;

import com.insulin.exception.model.EmailAlreadyExistentException;
import com.insulin.exception.model.UserNotFoundException;
import com.insulin.exception.model.UsernameAlreadyExistentException;
import com.insulin.model.User;
import com.insulin.model.UserPrincipal;
import com.insulin.repository.UserRepository;
import com.insulin.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Date;
import java.util.Objects;

import static com.insulin.shared.UserConstants.*;

@Service
@Transactional
@Qualifier("userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        user.getDetails()
                .setLastLoginDateDisplay(user.getDetails().getLastLoginDate()); //last login
        user.getDetails().setLastLoginDate(new Date()); // current login, now
        userRepository.save(user); //update information
        logger.info("Returning found user");
        return new UserPrincipal(user);
    }

    @Override
    public User findUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    private void validateNewUsernameAndEmail(String username, String email) throws UserNotFoundException, UsernameAlreadyExistentException, EmailAlreadyExistentException {
        if(!StringUtils.isNotEmpty(username) || !StringUtils.isNotEmpty(email)) {
            throw new UserNotFoundException(INVALID_DATA);
        }
        User userByUsername = findUserByUsername(username);
        if (userByUsername != null) {
            throw new UsernameAlreadyExistentException(USERNAME_ALREADY_EXISTENT);
        }
        User userByEmail = findUserByEmail(email);
        if (userByEmail != null) {
            throw new EmailAlreadyExistentException(EMAIL_ALREADY_EXISTENT);
        }
    }
}
