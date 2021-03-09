package com.insulin.shared;

/**
 * A bunch of constant used for security configuration or to specify some time durations.
 */
public class SecurityConstants {
    public static final long JWT_EXPIRATION_TIME = 900_000;  // 15 minutes in milliseconds
    public static final long REFRESH_EXPIRATION_TIME_MS = 604_800_000L; // 7 days in ms
    public static final int REFRESH_EXPIRATION_TIME_SEC = 604_800; // 7 days in seconds
    public static final long RESET_PASSWORD_LIFE = 10_800L;

    public static final String TOKEN_PREFIX = "Bearer "; // what kind of token we have
    public static final String JWT_TOKEN_HEADER = "Jwt-Token"; // for header
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified"; // error message
    public static final String ISSUER = "Insulin Sensitivity INC"; // who create it
    public static final String AUDIENCE = "Users"; // optional
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to be logged-in in order to access this page."; // error message access
    public static final String ACCESS_DENIED_MESSAGE = "You don't have the permission to access this page."; // another error
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/login", "/register", "/autologin", "/forgotPassword/**",
            "/resetPassword/**", "/user/resetPassword/**", "/user/image/**"}; // set of urls which are permitted by default.
}
