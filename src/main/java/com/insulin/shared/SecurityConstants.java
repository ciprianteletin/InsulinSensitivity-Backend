package com.insulin.shared;

public class SecurityConstants {
    public static final long JWT_EXPIRATION_TIME = 900_000;  // 15 minutes in milliseconds
    public static final long REFRESH_EXPIRATION_TIME = 864_000_00; // one day in ms
    public static final String TOKEN_PREFIX = "Bearer "; //tipul de token, prefixul
    public static final String JWT_TOKEN_HEADER = "Jwt-Token"; //pentru header
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified"; //error message
    public static final String ISSUER = "Insulin Sensitivity INC"; //cine emite
    public static final String AUDIENCE = "User Admin"; //optional
    //TODO implements for the doctor, patient. Or remove it if not needed
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to be logged-in in order to access this page."; //error message access
    public static final String ACCESS_DENIED_MESSAGE = "You don't have the permission to access this page."; //another error
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/login", "/register", "/user/resetpassword/**", "/user/image/**"}; //url-uri ce nu vor fi blocate de security
}
