package com.insulin.controller;

import com.insulin.configs.JwtTokenProvider;
import com.insulin.exception.model.EmailAlreadyExistentException;
import com.insulin.exception.model.UserNotFoundException;
import com.insulin.exception.model.UsernameAlreadyExistentException;
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

import static com.insulin.shared.SecurityConstants.*;
import static com.insulin.utils.AuthenticationUtils.*;
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
        passHttpOnlyCookie("refreshToken", metaInformation.getRefreshToken(), REFRESH_EXPIRATION_TIME_SEC, response);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@Valid @RequestBody CompleteUser completeUser)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException, MessagingException {
        authService.register(completeUser);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User registered successfully");
    }

    /**
     * Update the user password in case he forgot the old one. For this operation, the user cannot be logged in.
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<HttpResponse> resetPassword(@Valid @RequestBody User user) {
        authService.resetPassword(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Send to the user an email to the provided mail address which will redirect the user to a form where
     * he can reset the password. Each link is available to use for 3 hours before it will be
     * unavailable.
     */
    @GetMapping("/forgotPassword/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable String email, HttpServletResponse response) throws MessagingException {
        String secretForgot = authService.redirectResetPassword(email);
        passHttpOnlyCookie("forgotPassword", secretForgot, RESET_PASSWORD_LIFE, response);
        return new ResponseEntity<>(secretForgot, HttpStatus.OK);
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
        MetaInformation metaInformation = createMetaDataInformation(null, request); //we don't know who the user is
        MetaInformation storedInformation = metaInformationService.findByRefreshTokenAndDevice(refreshToken, metaInformation.getDeviceInformation());
        if (nonNull(storedInformation) && refreshToken.equals(storedInformation.getRefreshToken())) {
            //Then the user was logged on his device and has the good token.
            User user = authService.findUserById(storedInformation.getUserId());
            UserPrincipal userPrincipal = new UserPrincipal(user);
            HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
            return new ResponseEntity<>(user, jwtHeader, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/logout/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<HttpResponse> logout(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        MetaInformation metaInformation = createMetaDataInformation(id, request);
        metaInformationService.deleteByUserIdAndDeviceDetails(id, metaInformation.getDeviceInformation());
        deleteCookie("refreshToken", response);
        deleteCookie("forgotPassword", response);
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
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<HttpStatus> generateToken(@CookieValue("refreshToken") String refreshToken,
                                                    @PathVariable("id") Long id,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    Authentication auth) {
        MetaInformation dbMetaInformation = metaInformationService.findByUserIdAndRefreshToken(id, refreshToken);
        String username = (String) auth.getPrincipal(); // can be email or actual username
        UserPrincipal loggedUser = new UserPrincipal(authService.findUserByUsernameOrEmail(username));
        if (dbMetaInformation == null) {
            MetaInformation metaInformation = saveMetaDataInformation(id, request);
            passHttpOnlyCookie("refreshToken", metaInformation.getRefreshToken(), REFRESH_EXPIRATION_TIME_SEC, response);
        }
        HttpHeaders jwtHeader = getJwtHeader(loggedUser);
        return new ResponseEntity<>(jwtHeader, HttpStatus.OK);
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
