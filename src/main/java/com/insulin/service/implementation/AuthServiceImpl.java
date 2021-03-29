package com.insulin.service.implementation;

import com.insulin.exception.model.*;
import com.insulin.metadata.LostUser;
import com.insulin.model.User;
import com.insulin.model.UserDetail;
import com.insulin.model.UserPrincipal;
import com.insulin.repository.AuthRepository;
import com.insulin.service.EmailService;
import com.insulin.service.abstraction.AuthService;
import com.insulin.service.abstraction.LostUserService;
import com.insulin.service.abstraction.UserManagerService;
import com.insulin.utils.AuthenticationUtils;
import com.insulin.utils.ValidationUtils;
import com.insulin.utils.model.CompleteUser;
import com.insulin.utils.abstractions.AbstractUserFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static com.insulin.shared.ExceptionConstants.OLD_PASSWORD;
import static com.insulin.utils.AuthenticationUtils.encryptPassword;
import static java.util.Objects.isNull;

@Service
@Transactional
@Qualifier("userService")
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AuthRepository authRepository;
    private final AbstractUserFactory userFactory;
    private final EmailService emailService;
    private final LostUserService lostUserService;
    private final ValidationUtils validationUtils;
    private final UserManagerService userManagerService;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository,
                           AbstractUserFactory userFactory,
                           EmailService emailService,
                           LostUserService lostUserService,
                           ValidationUtils validationUtils,
                           UserManagerService userManagerService) {
        this.authRepository = authRepository;
        this.userFactory = userFactory;
        this.emailService = emailService;
        this.lostUserService = lostUserService;
        this.validationUtils = validationUtils;
        this.userManagerService = userManagerService;
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
        user.getDetails()
                .setLastLoginDateDisplay(user.getDetails().getLastLoginDate()); //last login
        user.getDetails().setLastLoginDate(LocalDate.now()); // current login, now
        authRepository.save(user); //update information
        logger.info("Returning found user");
        return principal;
    }

    @Override
    public User findUserByUsernameOrEmail(String text) {
        return userManagerService.findUserByUsernameOrEmail(text);
    }

    @Override
    public User findUserById(Long id) throws UserNotFoundException {
        return authRepository.findById(id) //
                .orElseThrow(() -> new UserNotFoundException("User not found for the specified id. Auto-login failed"));
    }

    @Override
    public void resetPassword(String password, String code) throws EmailNotFoundException, LinkExpiredException, OldPasswordException {
        ImmutablePair<String, String> lostUserInfoPair = getEmailFromLostUser(code);
        String email = AuthenticationUtils.decryptText(lostUserInfoPair.getRight());
        User forgottenUser = this.findUserByEmail(email);
        if (isNull(forgottenUser)) {
            throw new EmailNotFoundException("The provided email was not mapped to any user!");
        }
        if (this.validationUtils.checkSamePassword(password, forgottenUser.getPassword())) {
            throw new OldPasswordException(OLD_PASSWORD);
        }
        String encryptedPassword = encryptPassword(password);
        forgottenUser.setPassword(encryptedPassword);
        authRepository.save(forgottenUser);
        lostUserService.deleteById(lostUserInfoPair.getLeft());
    }

    /**
     * The secretCode and the email are saved inside the database so that we can check easily
     * if the secretCode from the link matches the one stored inside the database.
     * Once used, this information will be removed.
     */
    @Override
    public void redirectResetPassword(String email) throws InvalidEmailForgotPassword {
        User existUser = authRepository.findUserByEmail(email);
        if (isNull(existUser)) {
            throw new InvalidEmailForgotPassword("The provided email does not exist!");
        }
        String secretParam = RandomStringUtils.randomAlphanumeric(15);
        emailService.sendResetPasswordEmail(email, secretParam);
        lostUserService.save(email, secretParam);
    }

    @Override
    public void register(CompleteUser completeUser) throws UserNotFoundException, EmailAlreadyExistentException,
            UsernameAlreadyExistentException, PhoneNumberUniqueException {
        validationUtils.validateCompleteInformation(completeUser.getUsername(),
                completeUser.getEmail(), completeUser.getPhoneNr());
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

    private ImmutablePair<String, String> getEmailFromLostUser(String code) throws LinkExpiredException {
        LostUser lostUser = lostUserService.findByCode(code);
        if (isNull(lostUser)) {
            throw new LinkExpiredException("Reset password link has expired!");
        }
        return new ImmutablePair<>(lostUser.getId(), lostUser.getEmail());
    }
}
