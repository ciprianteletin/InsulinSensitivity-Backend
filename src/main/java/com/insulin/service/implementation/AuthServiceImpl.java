package com.insulin.service.implementation;

import com.insulin.exceptions.model.*;
import com.insulin.metadata.LostUser;
import com.insulin.model.User;
import com.insulin.model.UserDetails;
import com.insulin.model.UserPrincipal;
import com.insulin.repository.UserRepository;
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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static com.insulin.shared.constants.ExceptionConstants.OLD_PASSWORD;
import static com.insulin.shared.constants.SecurityConstants.FLASK_API;
import static com.insulin.utils.ApiCommunicationUtils.getStringResponse;
import static com.insulin.utils.ApiCommunicationUtils.obtainGetRequest;
import static com.insulin.utils.AuthenticationUtils.encryptPassword;
import static java.util.Objects.isNull;

@Service
@Transactional
@Qualifier("userService")
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final AbstractUserFactory userFactory;
    private final EmailService emailService;
    private final LostUserService lostUserService;
    private final ValidationUtils validationUtils;
    private final UserManagerService userManagerService;

    @Autowired
    public AuthServiceImpl(AbstractUserFactory userFactory,
                           EmailService emailService,
                           UserRepository userRepository,
                           LostUserService lostUserService,
                           ValidationUtils validationUtils,
                           UserManagerService userManagerService) {
        this.userFactory = userFactory;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.lostUserService = lostUserService;
        this.validationUtils = validationUtils;
        this.userManagerService = userManagerService;
    }

    /**
     * Method used by Spring Security, to fetch the current user from
     * the database. Throws an error if the user is not existent. The user can be found based on his username/email
     */
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = this.userManagerService.findUserByUsernameOrEmail(username);
        logger.info("User found for username: " + username);
        UserPrincipal principal = new UserPrincipal(user);
        user.getDetails().setLastLoginDate(LocalDate.now()); // current login, now
        userRepository.save(user); //update information
        logger.info("Returning found user");
        return principal;
    }

    @Override
    public void resetPassword(String password, String code) throws EmailNotFoundException, LinkExpiredException, OldPasswordException {
        ImmutablePair<String, String> lostUserInfoPair = getEmailFromLostUser(code);
        String email = AuthenticationUtils.decryptText(lostUserInfoPair.getRight());
        User forgottenUser = this.userManagerService.findUserByEmail(email);
        if (isNull(forgottenUser)) {
            throw new EmailNotFoundException("The provided email was not mapped to any user!");
        }
        if (this.validationUtils.checkSamePassword(password, forgottenUser.getPassword())) {
            throw new OldPasswordException(OLD_PASSWORD);
        }
        String encryptedPassword = encryptPassword(password);
        forgottenUser.setPassword(encryptedPassword);
        userRepository.save(forgottenUser);
        lostUserService.deleteById(lostUserInfoPair.getLeft());
    }

    /**
     * The secretCode and the email are saved inside the database so that we can check easily
     * if the secretCode from the link matches the one stored inside the database.
     * Once used, this information will be removed.
     */
    @Override
    public void redirectResetPassword(String email) throws InvalidEmailForgotPassword {
        User existUser = userManagerService.findUserByEmail(email);
        if (isNull(existUser)) {
            throw new InvalidEmailForgotPassword("The provided email does not exist!");
        }
        String secretParam = RandomStringUtils.randomAlphanumeric(15);
        emailService.sendResetPasswordEmail(email, secretParam);
        lostUserService.save(email, secretParam);
    }

    /**
     * Calls the Flask server to check the password against all pwned password to check if it's secured or not
     * The call is made via HttpClient and we receive a HttpResponse.
     */
    @Override
    public int checkPassword(String password) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = obtainGetRequest(FLASK_API + "checkpassword/" + password);
        HttpResponse<String> response = getStringResponse(request);
        return (boolean) new JSONObject(response.body()).get("result") ? 1 : 0;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void register(CompleteUser completeUser) throws UserNotFoundException, EmailAlreadyExistentException,
            UsernameAlreadyExistentException, PhoneNumberUniqueException {
        validationUtils.validateCompleteInformation(completeUser.getUsername(),
                completeUser.getEmail(), completeUser.getPhoneNr());
        logger.info("Username and e-mail ok");
        User user = userFactory.createUser(completeUser);
        UserDetails userDetails = userFactory.createUserDetails(completeUser);
        user.setBidirectionalDetails(userDetails);
        userRepository.save(user);
        emailService.sendRegisterEmail(userDetails.getFirstName(), userDetails.getEmail());
        logger.info("User registered with success!");
    }

    private ImmutablePair<String, String> getEmailFromLostUser(String code) throws LinkExpiredException {
        LostUser lostUser = lostUserService.findByCode(code);
        if (isNull(lostUser)) {
            throw new LinkExpiredException("Reset password link has expired!");
        }
        return new ImmutablePair<>(lostUser.getId(), lostUser.getEmail());
    }
}
