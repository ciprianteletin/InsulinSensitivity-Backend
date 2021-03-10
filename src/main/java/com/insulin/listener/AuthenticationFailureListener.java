package com.insulin.listener;

import com.insulin.exception.model.ActivateCaptchaException;
import com.insulin.service.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationFailureListener {
    private final LoginAttemptService loginAttemptService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationFailureListener.class);

    @Autowired
    public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthFailure(AuthenticationFailureBadCredentialsEvent event) throws ActivateCaptchaException {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof String) {
            String username = (String) principal;
            logger.info("Authentication failed!");
            addCaptchaIfNeeded(username);
            loginAttemptService.addUserToLoginCache(username);
        }
    }

    private void addCaptchaIfNeeded(String username) throws ActivateCaptchaException {
        if (loginAttemptService.isExceededMaxAttempts(username)) {
            Optional.ofNullable(loginAttemptService.addMarkedUser(username))
                    .orElseThrow(() -> new ActivateCaptchaException("Exceeded numbers of login attempts!"));
        }
    }
}
