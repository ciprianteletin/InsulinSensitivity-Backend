package com.insulin.controllers;

import com.insulin.configs.JwtTokenProvider;
import com.insulin.exceptions.model.*;
import com.insulin.metadata.MetaInformation;
import com.insulin.model.User;
import com.insulin.model.UserPrincipal;
import com.insulin.service.abstraction.AuthService;
import com.insulin.service.abstraction.MetaInformationService;
import com.insulin.shared.HttpResponse;
import com.insulin.utils.HttpResponseUtils;
import com.insulin.utils.model.CompleteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static com.insulin.shared.constants.ExceptionConstants.UPDATE_DENIED;
import static com.insulin.shared.constants.SecurityConstants.*;
import static com.insulin.shared.constants.UserConstants.USER_NOT_FOUND;
import static com.insulin.utils.AuthenticationUtils.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Handles http request related to authentication process, including login, register, refreshToken, changePassword and so on.
 * Represents the gate between the user and the application, if he desires to use additional functionality.
 */
@RestController
@RequestMapping("")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider provider;
    private final MetaInformationService metaInformationService;

    @Autowired
    public AuthController(@Qualifier("userService") AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider provider,
                          MetaInformationService metaInformationService) {
        this.provider = provider;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.metaInformationService = metaInformationService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = authService.findUserByUsernameOrEmail(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        MetaInformation metaInformation = saveMetaDataInformation(loginUser.getId(), request);
        passHttpOnlyCookie(REFRESH_TOKEN_NAME, metaInformation.getRefreshToken(), REFRESH_EXPIRATION_TIME_SEC, response);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@Valid @RequestBody CompleteUser completeUser)
            throws UserNotFoundException, EmailAlreadyExistentException,
            UsernameAlreadyExistentException, MessagingException, PhoneNumberUniqueException {
        authService.register(completeUser);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User registered successfully");
    }

    @GetMapping("/checkPassword/{password}")
    public ResponseEntity<Integer> checkPassword(@PathVariable String password) throws InterruptedException, IOException, URISyntaxException {
        return new ResponseEntity<>(authService.checkPassword(password), HttpStatus.OK);
    }

    /**
     * Update the user password in case he forgot the old one. For this operation, the user cannot be logged in.
     */
    @GetMapping("/resetPassword/{code}/{password}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable String code, @PathVariable String password)
            throws EmailNotFoundException, LinkExpiredException, OldPasswordException {
        authService.resetPassword(password, code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Send to the user an email to the provided mail address which will redirect the user to a form where
     * he can reset the password. Each link is available to use for 3 hours before it will be
     * unavailable.
     */
    @GetMapping("/forgotPassword/{email}")
    public ResponseEntity<HttpResponse> forgotPassword(@PathVariable String email)
            throws MessagingException, InvalidEmailForgotPassword {
        authService.redirectResetPassword(email);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "Reset password email sent!");
    }

    /**
     * If a refreshToken is existent, it means that the user was connected few days ago, so we can
     * use the auto-login functionality. Of course, we must check if the refreshToken matches the one
     * from the database, in case if the token is stolen by someone who might try to harm the user.
     * If there is no matching token, nothing will happen.
     * <p>
     * This request is called when the application start, or better to say, when the user
     * opens the application.
     */
    @GetMapping("/autologin")
    public ResponseEntity<User> autoLogin(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                          HttpServletRequest request) throws UserNotFoundException {
        if (refreshToken == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        MetaInformation metaInformation = createMetaDataInformation(null, request); // we don't know who the user is
        MetaInformation storedInformation = metaInformationService //
                .findByRefreshTokenAndDevice(refreshToken, metaInformation.getDeviceInformation());
        if (nonNull(storedInformation) && refreshToken.equals(storedInformation.getRefreshToken())) {
            //Then the user was logged on his device and has the good token.
            User user = authService.findUserById(storedInformation.getUserId());
            updateUserDetails(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
            return new ResponseEntity<>(user, jwtHeader, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Method used in the case of updating the username of the account, so that the Jwt-Token will contain accurate
     * information. The difference between this method and "refresh/{id}" is that this one does not re-create the
     * meta data information if it's null.
     */
    @GetMapping("/updateToken/{username}")
    public ResponseEntity<HttpStatus> updateToken(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                  @PathVariable("username") String username) throws UserNotFoundException, UpdateDeniedException {
        if (refreshToken == null) {
            throw new UpdateDeniedException(UPDATE_DENIED);
        }
        User user = authService.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        MetaInformation storedInformation = metaInformationService
                .findByUserIdAndRefreshToken(user.getId(), refreshToken);
        if (nonNull(storedInformation) && refreshToken.equals(storedInformation.getRefreshToken())) {
            UserPrincipal principal = new UserPrincipal(user);
            HttpHeaders jwtHeader = getJwtHeader(principal);
            return new ResponseEntity<>(jwtHeader, HttpStatus.OK);
        }
        throw new UpdateDeniedException(UPDATE_DENIED);
    }

    /**
     * The logout process deletes everything related to the metaInformation of the user, so that if
     * he decide to logout manually, the autologin process should not be applied. For this method,
     * the user must be logged in. The refreshToken is also removed.
     */
    @GetMapping("/logout/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN')")
    public ResponseEntity<HttpResponse> logout(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        MetaInformation metaInformation = createMetaDataInformation(id, request);
        metaInformationService.deleteByUserIdAndDeviceDetails(id, metaInformation.getDeviceInformation());
        deleteHttpOnlyCookie(REFRESH_TOKEN_NAME, response);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User logged out successfully!");
    }

    /**
     * Generates a new token based on the refreshToken and the user id. For this endpoint, the user must be logged in, so that
     * there is no need to filter after the metaInformation. For autologin, the metaInformation and the refreshToken will be used.
     * For this case, we take in consideration two cases:
     * 1. The refreshToken and the id of the user are in the database. Case when, a new token is emitted.
     * 2. The refreshToken and the id are expired, but the user is logged in, so that we will generate a new refresh token.
     */
    @GetMapping("/refresh/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN')")
    public ResponseEntity<HttpStatus> generateToken(@CookieValue("refreshToken") String refreshToken,
                                                    @PathVariable("id") Long id,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    Authentication auth) {
        MetaInformation dbMetaInformation = metaInformationService //
                .findByUserIdAndRefreshToken(id, refreshToken);
        String username = (String) auth.getPrincipal(); // can be email or actual username
        UserPrincipal loggedUser = new UserPrincipal(authService.findUserByUsernameOrEmail(username));
        if (isNull(dbMetaInformation)) {
            MetaInformation metaInformation = saveMetaDataInformation(id, request);
            passHttpOnlyCookie(REFRESH_TOKEN_NAME, metaInformation.getRefreshToken(), REFRESH_EXPIRATION_TIME_SEC, response);
        }
        HttpHeaders jwtHeader = getJwtHeader(loggedUser);
        return new ResponseEntity<>(jwtHeader, HttpStatus.OK);
    }

    private void updateUserDetails(User user) {
        user.getDetails().setLastLoginDate(LocalDate.now());
        authService.save(user);
    }

    private MetaInformation saveMetaDataInformation(Long userId, HttpServletRequest request) {
        MetaInformation metaInformation = createMetaDataInformation(userId, request);
        metaInformationService.save(metaInformation);
        return metaInformation;
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, provider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
