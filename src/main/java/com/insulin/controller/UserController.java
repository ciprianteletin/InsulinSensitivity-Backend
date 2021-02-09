package com.insulin.controller;

import com.insulin.exception.model.EmailAlreadyExistentException;
import com.insulin.exception.model.UserNotFoundException;
import com.insulin.exception.model.UsernameAlreadyExistentException;
import com.insulin.service.UserService;
import com.insulin.shared.HttpResponse;
import com.insulin.utils.CompleteUser;
import com.insulin.utils.HttpResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(@Qualifier("userService")UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@RequestBody CompleteUser completeUser)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException {
        userService.register(completeUser);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User registered successfully");
    }
}
