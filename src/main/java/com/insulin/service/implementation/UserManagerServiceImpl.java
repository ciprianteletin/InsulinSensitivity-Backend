package com.insulin.service.implementation;

import com.insulin.exceptions.model.UserNotFoundException;
import com.insulin.model.User;
import com.insulin.repository.UserRepository;
import com.insulin.service.abstraction.UserManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.insulin.shared.constants.UserConstants.USER_NOT_FOUND;
import static com.insulin.utils.AuthenticationUtils.checkIfEmail;
import static java.util.Objects.isNull;

/**
 * This class has the purpose of being the central place for getting users, no matter
 * of what type of login they used(via email or via username). Before this class,
 * the method findUserByUsernameOrEmail was stored in AuthService, but other
 * classes needed this method.
 */
@Service
public class UserManagerServiceImpl implements UserManagerService {
    private final Logger logger = LoggerFactory.getLogger(UserManagerServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserManagerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByUsernameOrEmail(String text) {
        User user;
        if (checkIfEmail(text)) {
            user = userRepository.findUserByEmail(text);
            if (isNull(user)) {
                logger.error(USER_NOT_FOUND + ": " + text);
                throw new UsernameNotFoundException(USER_NOT_FOUND + " for email address " + text);
            }
            return user;
        }
        user = userRepository.findByUsername(text).orElseThrow(() -> {
            logger.error(USER_NOT_FOUND + ": " + text);
            throw new UsernameNotFoundException(USER_NOT_FOUND + ": " + text);
        });
        return user;
    }

    @Override
    public User findUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id) //
                .orElseThrow(() -> new UserNotFoundException("User not found for the specified id. Auto-login failed"));
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
