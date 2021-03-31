package com.insulin.controller;

import com.insulin.exception.model.*;
import com.insulin.model.User;
import com.insulin.service.abstraction.UserService;
import com.insulin.shared.HttpResponse;
import com.insulin.utils.HttpResponseUtils;
import com.insulin.utils.model.BasicUserInfo;
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

import static com.insulin.shared.UserConstants.USER_UPDATED;
import static java.util.Objects.isNull;

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
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, USER_UPDATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<HttpResponse> deleteUserById(@PathVariable("id") Long id,
                                                       Authentication auth,
                                                       HttpServletRequest request)
            throws UserNotFoundException, InvalidDataException {
        String principal = (String) auth.getPrincipal();
        User currentUser = this.userService.getUserById(id);
        if (isNull(currentUser)) {
            logger.error("The provided id is invalid. User not found");
            throw new UserNotFoundException("The provided id does not map to any user");
        }

        if (!principal.equals(currentUser.getUsername()) || !principal.equals(currentUser.getDetails().getEmail())) {
            logger.error("A user could not delete another user account!");
            throw new InvalidDataException("The id does not match to the current account!");
        }

        this.userService.deleteUser(currentUser, request);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User deleted with success");
    }
}

// TODO check updated password with the old one.
