package com.insulin.controller;

import com.insulin.configs.JwtTokenProvider;
import com.insulin.exception.model.EmailAlreadyExistentException;
import com.insulin.exception.model.UserNotFoundException;
import com.insulin.exception.model.UsernameAlreadyExistentException;
import com.insulin.model.User;
import com.insulin.model.UserPrincipal;
import com.insulin.service.abstraction.AuthService;
import com.insulin.shared.HttpResponse;
import com.insulin.utils.model.CompleteUser;
import com.insulin.utils.HttpResponseUtils;
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
import javax.validation.Valid;

import static com.insulin.shared.SecurityConstants.JWT_TOKEN_HEADER;

@RestController
@RequestMapping("")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider provider;

    @Autowired
    public AuthController(@Qualifier("userService") AuthService authService, AuthenticationManager authenticationManager, JwtTokenProvider provider) {
        this.provider = provider;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = authService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@Valid @RequestBody CompleteUser completeUser)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException, MessagingException {
        authService.register(completeUser);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User registered successfully");
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