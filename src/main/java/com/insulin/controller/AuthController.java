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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.insulin.shared.SecurityConstants.JWT_TOKEN_HEADER;
import static com.insulin.utils.AuthenticationUtils.createMetaDataInformation;

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
        System.out.println(metaInformationService.findById(metaInformation.getId()));
        passHttpOnlyCookie(metaInformation, response);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@Valid @RequestBody CompleteUser completeUser)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException, MessagingException {
        authService.register(completeUser);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User registered successfully");
    }

    @GetMapping("/logout/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<HttpResponse> logout(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        MetaInformation metaInformation = createMetaDataInformation(id, request);
        metaInformationService.deleteByUserIdAndDeviceDetails(id, metaInformation.getDeviceInformation());
        deleteCookie(response);
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
            passHttpOnlyCookie(metaInformation, response);
        }
        HttpHeaders jwtHeader = getJwtHeader(loggedUser);
        return new ResponseEntity<>(jwtHeader, HttpStatus.OK);
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
        cookie.setSecure(false);
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
        cookie.setSecure(false);
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
