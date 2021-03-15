package com.insulin.controller;

import com.insulin.exception.model.InvalidDataException;
import com.insulin.exception.model.UserNotFoundException;
import com.insulin.model.User;
import com.insulin.service.abstraction.UserService;
import com.insulin.shared.HttpResponse;
import com.insulin.utils.HttpResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

        if (!principal.equals(currentUser.getUsername())) {
            logger.error("A user could not delete another user account!");
            throw new InvalidDataException("The id does not match to the current account!");
        }

        this.userService.deleteUser(currentUser, request);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User deleted with success");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }
}
