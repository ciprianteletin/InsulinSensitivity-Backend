package com.insulin.service.implementation;

import com.insulin.exception.model.EmailAlreadyExistentException;
import com.insulin.exception.model.PhoneNumberUniqueException;
import com.insulin.exception.model.UserNotFoundException;
import com.insulin.exception.model.UsernameAlreadyExistentException;
import com.insulin.metadata.MetaInformation;
import com.insulin.model.User;
import com.insulin.model.UserDetail;
import com.insulin.repository.UserRepository;
import com.insulin.service.EmailService;
import com.insulin.service.abstraction.LostUserService;
import com.insulin.service.abstraction.MetaInformationService;
import com.insulin.service.abstraction.UserManagerService;
import com.insulin.service.abstraction.UserService;
import com.insulin.utils.ValidationUtils;
import com.insulin.utils.model.BasicUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.insulin.shared.UserConstants.USERNAME_NOT_FOUND;
import static com.insulin.shared.UserConstants.USER_NOT_FOUND;
import static com.insulin.utils.AuthenticationUtils.updatePrincipal;
import static com.insulin.utils.AuthenticationUtils.verifyPrincipalChange;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final MetaInformationService metaInformationService;
    private final LostUserService lostUserService;
    private final ValidationUtils validationUtils;
    private final UserManagerService userManagerService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           EmailService emailService,
                           MetaInformationService metaInformationService,
                           LostUserService lostUserService,
                           ValidationUtils validationUtils,
                           UserManagerService userManagerService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.metaInformationService = metaInformationService;
        this.lostUserService = lostUserService;
        this.validationUtils = validationUtils;
        this.userManagerService = userManagerService;
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
    public String getUserIpAddress(String username) {
        User user = userManagerService.findUserByUsernameOrEmail(username); // can be username or email
        Long id = user.getId();
        Optional<MetaInformation> metaInformation = metaInformationService.findByUserId(id) //
                .stream().findAny();
        return metaInformation.map(MetaInformation::getIp) //
                .orElse("Invalid IP!");
    }

    @Override
    @CachePut(value = "users", key = "#id", cacheManager = "cacheManager")
    public void updateUser(Long id, BasicUserInfo basicUserInfo)
            throws UserNotFoundException, EmailAlreadyExistentException,
            UsernameAlreadyExistentException, PhoneNumberUniqueException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        boolean modifyPrincipal = verifyPrincipalChange(user, basicUserInfo);
        this.checkUserInformation(user, basicUserInfo);
        if (modifyPrincipal) {
            updatePrincipal(user);
        }
        this.userRepository.save(user);
    }

    private void checkUserInformation(User user, BasicUserInfo basicUserInfo)
            throws UserNotFoundException, UsernameAlreadyExistentException,
            EmailAlreadyExistentException, PhoneNumberUniqueException {
        UserDetail userDetail = user.getDetails();

        String updatedUsername = basicUserInfo.getUsername();
        this.checkAndSetUsername(user, updatedUsername);

        String updatedEmail = basicUserInfo.getEmail();
        this.checkAndSetEmail(userDetail, updatedEmail);

        String phoneNr = basicUserInfo.getPhoneNr();
        this.checkAndSetPhoneNr(userDetail, phoneNr);
    }

    private void checkAndSetUsername(User user, String updatedUsername)
            throws UserNotFoundException, UsernameAlreadyExistentException {
        if (!user.getUsername().equals(updatedUsername)) {
            this.validationUtils.validateNewUsername(updatedUsername);
            user.setUsername(updatedUsername);
        }
    }

    private void checkAndSetEmail(UserDetail userDetail, String updatedEmail)
            throws EmailAlreadyExistentException, UserNotFoundException {
        if (!userDetail.getEmail().equals(updatedEmail)) {
            this.validationUtils.validateNewEmail(updatedEmail);
            lostUserService.deleteByEmail(userDetail.getEmail());
            userDetail.setEmail(updatedEmail);
        }
    }

    private void checkAndSetPhoneNr(UserDetail userDetail, String phoneNr)
            throws PhoneNumberUniqueException {
        if (!userDetail.getPhoneNr().equals(phoneNr)) {
            this.validationUtils.validatePhoneNumber(phoneNr);
            userDetail.setPhoneNr(phoneNr);
        }
    }
}
