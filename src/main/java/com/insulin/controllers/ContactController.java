package com.insulin.controllers;

import com.insulin.service.EmailService;
import com.insulin.utils.model.ContactModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
public class ContactController {
    private final EmailService emailService;

    @Autowired
    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> sendContactEmail(@RequestBody ContactModel contactModel) {
        String emailMessage = convertContactModelToMessage(contactModel);
        emailService.sendContactEmail(emailMessage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String convertContactModelToMessage(ContactModel contactModel) {
        return  "We receive a message from " + contactModel.getName() +
                " with the email: " + contactModel.getEmail() +
                " and phone number: " + contactModel.getPhone() +
                "\n\n" + contactModel.getMessage();
    }
}
