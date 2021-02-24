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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.insulin.shared.SecurityConstants.JWT_TOKEN_HEADER;
import static com.insulin.utils.AuthenticationUtils.createMetaDataInformation;

/**
 * Handles http request related to authentication process, including login, register, refreshToken, changePassword and so on.
 * Represents the gate between the user and the application, if he desires to use additional functionality
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
        MetaInformation metaInformation = saveMetaDataInformation(user.getId(), request);
        passHttpOnlyCookie(metaInformation, response);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@Valid @RequestBody CompleteUser completeUser)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException, MessagingException {
        authService.register(completeUser);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User registered successfully");
    }

    private MetaInformation saveMetaDataInformation(Long userId, HttpServletRequest request) {
        MetaInformation metaInformation = createMetaDataInformation(userId, request);
        metaInformationService.save(metaInformation);
        return metaInformation;
    }

    /**
     * Creates a cookie for the refresh token with a duration of life of 7 days. By making it
     * httpOnly, the cookie would be read only by the server side, making it secure against
     * attacks like XSS
     */
    private void passHttpOnlyCookie(MetaInformation metaInformation, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", metaInformation.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60); //7 days in seconds
        cookie.setPath("/"); //accessible everywhere, change for a stable path
        response.addCookie(cookie);
    }

    /**
     * For deleting a cookie, we must set the value as null to the same key and also
     * set the life of the cookie as 0. The same set of property must be used for
     * both cookies.
     */
    private void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
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
