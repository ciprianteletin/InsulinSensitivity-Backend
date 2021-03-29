package com.insulin.service.implementation;

import com.insulin.model.User;
import com.insulin.repository.AuthRepository;
import com.insulin.service.abstraction.UserManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.insulin.shared.UserConstants.USER_NOT_FOUND;
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
    private final AuthRepository authRepository;

    @Autowired
    public UserManagerServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
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
}
