package com.insulin.controllers;

import com.insulin.exceptions.model.*;
import com.insulin.model.User;
import com.insulin.service.abstraction.UserService;
import com.insulin.shared.HttpResponse;
import com.insulin.utils.HttpResponseUtils;
import com.insulin.utils.model.BasicUserInfo;
import com.insulin.utils.model.UserPasswordInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.insulin.shared.constants.UserConstants.USER_UPDATED;
import static com.insulin.utils.ValidationUtils.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("username/{username}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/ip")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<String> getUserIP(Authentication authentication) throws UserNotFoundException {
        String username = (String) authentication.getPrincipal();
        return new ResponseEntity<>(userService.getUserIpAddress(username), HttpStatus.OK);
    }

    @PutMapping("/updateInformation/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<HttpResponse> updateUserInformation(@PathVariable("id") Long id,
                                                              @Valid @RequestBody BasicUserInfo basicUserInfo)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException, PhoneNumberUniqueException {
        this.userService.updateUser(id, basicUserInfo);
        logger.info("User updated!");
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, USER_UPDATED);
    }

    @PutMapping("updatePassword")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<HttpResponse> updateUserPassword(@Valid @RequestBody UserPasswordInfo userPasswordInfo,
                                                           Authentication authentication) throws OldPasswordException {
        User user = userService.getUserByUsername(userPasswordInfo.getUsername());
        String principal = (String) authentication.getPrincipal();
        this.userService.updatePassword(user, principal, userPasswordInfo);
        logger.info("Password updated!");
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, USER_UPDATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<HttpResponse> deleteUserById(@PathVariable("id") Long id,
                                                       Authentication auth,
                                                       HttpServletRequest request)
            throws UserNotFoundException {
        String principal = (String) auth.getPrincipal();
        User currentUser = this.userService.getUserById(id);
        validateNonNullUser(currentUser);
        this.userService.deleteUser(currentUser, principal, request);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User deleted with success");
    }
}
