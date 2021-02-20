package com.insulin.listener;

import com.insulin.model.UserPrincipal;
import com.insulin.service.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {
    private final LoginAttemptService loginAttemptService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessListener.class);

    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    /**
     * Method which listen to the event that is fired when the user login with success.
     * In this case, we want to remove the user from the cache.
     */
    @EventListener
    public void onAuthSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            UserPrincipal user = (UserPrincipal) principal;
            logger.info("Authentication with success!");
            loginAttemptService.evictUserFromLoginCache(user.getUsername());
        }
    }
}
