package com.insulin.utils;

import com.insulin.metadata.MetaInformation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static com.insulin.shared.SecurityConstants.REFRESH_EXPIRATION_TIME_MS;
import static com.insulin.utils.RequestUtils.getRemoteIP;

/**
 * Utility class with reusable methods related to authentication. Since the AuthController has as main scope to treat request of any kind,
 * adding new functionality would break Single Resp. Principle, hence creating a new class.
 */
public class AuthenticationUtils {
    private static final EmailValidator emailValidator;
    private static final BCryptPasswordEncoder encoder;

    static {
        emailValidator = EmailValidator.getInstance();
        encoder = new BCryptPasswordEncoder();
    }

    public static String getDeviceDetails(String userAgent) {
        StringBuilderUtils deviceBuilder = new StringBuilderUtils();
        Client client = new Parser().parse(userAgent);
        if (Objects.nonNull(client)) {
            deviceBuilder.appendIfNotNull(client.userAgent.family, " ") //
                    .appendIfNotNull(client.userAgent.major, ".") //
                    .appendIfNotNull(client.userAgent.minor, "-") //
                    .appendIfNotNull(client.os.family, " ") //
                    .appendIfNotNull(client.os.major, ".") //
                    .appendIfNotNull(client.os.minor, "");
        }
        return deviceBuilder.getResult();
    }

    public static MetaInformation createMetaDataInformation(Long userId, HttpServletRequest request) {
        String randomToken = RandomStringUtils.randomAlphanumeric(25);
        String userAgent = request.getHeader("user-agent");
        String deviceDetails = getDeviceDetails(userAgent);
        String ip = getRemoteIP(RequestContextHolder.currentRequestAttributes());

        return MetaInformation.builder() //
                .userId(userId) //
                .refreshToken(randomToken) //
                .expirationTime(REFRESH_EXPIRATION_TIME_MS) // 7 days Expiration time, match the cookie duration)
                .deviceInformation(deviceDetails) //
                .ip(ip) //
                .build();
    }

    /**
     * Creates a cookie which is httpOnly secure, meaning that only the browser can read it.
     * This cookie is usually used for storing data in secured ways.
     */
    public static void passHttpOnlyCookie(String name, String value, int age, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(age);
        cookie.setPath("/"); //accessible everywhere, change for a stable path
        response.addCookie(cookie);
    }

    /**
     * For deleting a cookie, we must set the value as null to the same key and also
     * set the life of the cookie as 0. The same set of property must be used for
     * both cookies.
     */
    public static void deleteHttpOnlyCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void passCookie(String name, String value, int age, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static boolean checkIfEmail(String text) {
        return emailValidator.isValid(text);
    }

    public static String encryptPassword(String password) {
        return encoder.encode(password);
    }
}
