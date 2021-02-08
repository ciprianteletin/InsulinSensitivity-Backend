package com.insulin.service.implementation;

import com.insulin.model.User;
import com.insulin.model.UserPrincipal;
import com.insulin.repository.UserRepository;
import com.insulin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
@Qualifier("userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;

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
            logger.error("User not found: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        logger.info("User found for username: " + username);
        user.getDetails()
                .setLastLoginDateDisplay(user.getDetails().getLastLoginDate()); //last login
        user.getDetails().setLastLoginDate(new Date()); // current login, now
        userRepository.save(user); //update information
        logger.info("Returning found user");
        return new UserPrincipal(user);
    }
}
