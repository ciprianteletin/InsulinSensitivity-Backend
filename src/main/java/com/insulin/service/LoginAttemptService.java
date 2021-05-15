package com.insulin.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Service used in order to verify the number of failed attempts of login by an user. If the user fail
 * to login five times in a row, a captcha code will appear in the screen in order to prevent
 * brute force attacks that can be malicious to the user attached. For the cache, we use Guava library.
 */
@Service
public class LoginAttemptService {
    private static final int MAX_NUMBER_ATTEMPT = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private final LoadingCache<String, Integer> loginCache;

    /**
     * Creates the cache, which is local in our system. The expiration time is set to 15 minutes
     * and at any time, there are a maximum number of 100 users in cache.
     */
    public LoginAttemptService() {
        this.loginCache = CacheBuilder.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key){
                        return 0;
                    }
                });
    }

    /**
     * If the authentication is successful, the user will be removed from the cache.
     */
    public void evictUserFromLoginCaches(String username) {
        loginCache.invalidate(username);
    }

    /**
     * Add an user to the login case if auth fails.
     */
    public void addUserToLoginCache(String username) {
        int attempts = 0;
        try {
            attempts = ATTEMPT_INCREMENT + loginCache.get(username);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        loginCache.put(username, attempts);
    }

    /**
     * If the user number of login failure are greater than the Maximum number constant, then
     * the cache will be displayed.
     */
    public boolean isExceededMaxAttempts(String username) {
        try {
            return loginCache.get(username) >= MAX_NUMBER_ATTEMPT;
        } catch (ExecutionException e) {
            e.printStackTrace(); //should not happen
        }
        return false;
    }
}
