package com.insulin.controller;

import com.insulin.exception.model.UserNotFoundException;
import com.insulin.model.User;
import com.insulin.service.EmailService;
import com.insulin.service.abstraction.UserService;
import com.insulin.shared.HttpResponse;
import com.insulin.utils.HttpResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<HttpResponse> deleteUserById(@PathVariable("id") Long id) throws UserNotFoundException, MessagingException {
        User currentUser = this.userService.getUserById(id);
        if (currentUser == null) {
            logger.error("The provided id is invalid. User not found");
            throw new UserNotFoundException("The provided id does not map to any user");
        }
        this.emailService.sendDeleteEmail(currentUser.getDetails().getFirstName(), currentUser.getDetails().getEmail());
        logger.info("Delete message has been emitted");
        this.userService.deleteUser(id);
        return HttpResponseUtils.buildHttpResponseEntity(HttpStatus.OK, "User deleted with success");
    }
}
