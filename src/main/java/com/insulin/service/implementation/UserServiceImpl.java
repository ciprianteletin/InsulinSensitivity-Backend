package com.insulin.service.implementation;

import com.insulin.exception.model.UserNotFoundException;
import com.insulin.metadata.MetaInformation;
import com.insulin.model.User;
import com.insulin.repository.UserRepository;
import com.insulin.service.EmailService;
import com.insulin.service.abstraction.LostUserService;
import com.insulin.service.abstraction.MetaInformationService;
import com.insulin.service.abstraction.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.insulin.shared.UserConstants.USERNAME_NOT_FOUND;
import static com.insulin.shared.UserConstants.USER_NOT_FOUND;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final MetaInformationService metaInformationService;
    private final LostUserService lostUserService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           EmailService emailService,
                           MetaInformationService metaInformationService,
                           LostUserService lostUserService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.metaInformationService = metaInformationService;
        this.lostUserService = lostUserService;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true, cacheManager = "cacheManager")
    public void deleteUser(User currentUser, HttpServletRequest request) {
        this.emailService.sendDeleteEmail(currentUser.getDetails().getFirstName(), currentUser.getDetails().getEmail());
        logger.info("Delete message has been emitted");
        Long id = currentUser.getId();
        metaInformationService.deleteByUserId(id);
        logger.info("MetaInformation was deleted");
        lostUserService.deleteByEmail(currentUser.getDetails().getEmail());
        logger.info("Reset password data was deleted!");
        this.userRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "users", key = "#id", cacheManager = "cacheManager")
    public User getUserById(Long id) throws UserNotFoundException {
        Optional<User> currentUser = this.userRepository.findById(id);
        return currentUser.orElseThrow(() -> new UserNotFoundException("User not found for the provided id"));
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> currentUser = this.userRepository.findByUsername(username);
        return currentUser.orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
    }

    @Override
    public String getUserIpAddress(String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        Long id = user.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND)) //
                .getId();
        Optional<MetaInformation> metaInformation = metaInformationService.findByUserId(id) //
                .stream().findAny();
        return metaInformation.map(MetaInformation::getIp) //
                .orElse("Invalid IP!");
    }
}
