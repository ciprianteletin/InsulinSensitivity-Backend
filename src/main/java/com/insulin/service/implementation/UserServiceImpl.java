package com.insulin.service.implementation;

import com.insulin.exceptions.model.*;
import com.insulin.metadata.MetaInformation;
import com.insulin.model.User;
import com.insulin.model.UserDetails;
import com.insulin.repository.UserRepository;
import com.insulin.service.EmailService;
import com.insulin.service.abstraction.LostUserService;
import com.insulin.service.abstraction.MetaInformationService;
import com.insulin.service.abstraction.UserManagerService;
import com.insulin.service.abstraction.UserService;
import com.insulin.utils.AuthenticationUtils;
import com.insulin.utils.ByteDecompressor;
import com.insulin.utils.ValidationUtils;
import com.insulin.utils.model.BasicUserInfo;
import com.insulin.utils.model.UserPasswordInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

import static com.insulin.shared.constants.UserConstants.USERNAME_NOT_FOUND;
import static com.insulin.utils.AuthenticationUtils.*;

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
    public void deleteUser(User currentUser, String principal, HttpServletRequest request) {
        validateUserAuthenticity(currentUser, principal);
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
        Optional<User> currentUser = this.userManagerService.findUserByUsername(username);
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

    /**
     * Update the user information. After update, a value must be returned so that the cache will be updated.
     */
    @Override
    @CachePut(value = "users", key = "#id", cacheManager = "cacheManager")
    public User updateUser(Long id, BasicUserInfo basicUserInfo)
            throws UserNotFoundException, EmailAlreadyExistentException,
            UsernameAlreadyExistentException, PhoneNumberUniqueException {
        User user = userManagerService.findUserById(id);
        boolean modifyPrincipal = verifyPrincipalChange(user, basicUserInfo);
        this.checkUserInformation(user, basicUserInfo);
        this.userRepository.save(user);
        if (modifyPrincipal) {
            updatePrincipal(user);
        }
        return user;
    }

    @Override
    @CachePut(value = "users", key = "#id", cacheManager = "cacheManager")
    public User updateProfileImage(Long id, MultipartFile file) throws UserNotFoundException, IOException {
        User user = userManagerService.findUserById(id);
        UserDetails details = user.getDetails();
        byte[] compressedImage = ByteDecompressor.compressBytes(file.getBytes());
        details.setProfileImage(compressedImage);
        userRepository.save(user);
        return user;
    }

    @Override
    public void updatePassword(User user, String principal, UserPasswordInfo userPasswordInfo) throws OldPasswordException {
        validateUserAuthenticity(user, principal);
        String newPassword = userPasswordInfo.getNewPassword();
        this.validationUtils.validateIdenticalPassword(user.getPassword(), userPasswordInfo.getPassword());
        this.validationUtils.validatePasswordNotIdentical(user.getPassword(), newPassword);
        newPassword = AuthenticationUtils.encryptPassword(newPassword);
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    private void checkUserInformation(User user, BasicUserInfo basicUserInfo)
            throws UserNotFoundException, UsernameAlreadyExistentException,
            EmailAlreadyExistentException, PhoneNumberUniqueException {
        UserDetails userDetails = user.getDetails();

        String updatedUsername = basicUserInfo.getUsername();
        this.checkAndSetUsername(user, updatedUsername);

        String updatedEmail = basicUserInfo.getEmail();
        this.checkAndSetEmail(userDetails, updatedEmail);

        String phoneNr = basicUserInfo.getPhoneNr();
        this.checkAndSetPhoneNr(userDetails, phoneNr);
    }

    private void checkAndSetUsername(User user, String updatedUsername)
            throws UserNotFoundException, UsernameAlreadyExistentException {
        if (!user.getUsername().equals(updatedUsername)) {
            this.validationUtils.validateNewUsername(updatedUsername);
            user.setUsername(updatedUsername);
        }
    }

    private void checkAndSetEmail(UserDetails userDetails, String updatedEmail)
            throws EmailAlreadyExistentException, UserNotFoundException {
        if (!userDetails.getEmail().equals(updatedEmail)) {
            this.validationUtils.validateNewEmail(updatedEmail);
            lostUserService.deleteByEmail(userDetails.getEmail());
            userDetails.setEmail(updatedEmail);
        }
    }

    private void checkAndSetPhoneNr(UserDetails userDetails, String phoneNr)
            throws PhoneNumberUniqueException {
        if (!userDetails.getPhoneNr().equals(phoneNr)) {
            this.validationUtils.validatePhoneNumber(phoneNr);
            userDetails.setPhoneNr(phoneNr);
        }
    }
}
