package com.insulin.utils;

import com.insulin.metadata.MetaInformation;
import com.insulin.model.User;
import com.insulin.utils.model.BasicUserInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static com.insulin.utils.RequestUtils.getRemoteIP;

/**
 * Utility class with reusable methods related to authentication. Since the AuthController has as main scope to treat request of any kind,
 * adding new functionality would break Single Resp. Principle, hence creating a new class.
 */
public class AuthenticationUtils {
    private static final EmailValidator emailValidator;
    private static final BCryptPasswordEncoder encoder;
    private static final BasicTextEncryptor encrypter;

    static {
        emailValidator = EmailValidator.getInstance();
        encoder = new BCryptPasswordEncoder();
        encrypter = new BasicTextEncryptor();
        encrypter.setPassword("mySuperStrongPassword");
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
                .deviceInformation(deviceDetails) //
                .ip(ip) //
                .build();
    }

    public static boolean verifyPrincipalChange(User user, BasicUserInfo basicInfo) {
        String principal = (String) SecurityContextHolder.getContext() //
                .getAuthentication() //
                .getPrincipal();
        String username = user.getUsername();
        return (Objects.equals(username, principal) && !Objects.equals(username, basicInfo.getUsername()));
    }

    public static void updatePrincipal(User user) {
        PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(token);
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
        cookie.setDomain("localhost");
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
        cookie.setDomain("localhost");
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

    public static String encryptText(String text) {
        return encrypter.encrypt(text);
    }

    public static String decryptText(String text) {
        return encrypter.decrypt(text);
    }

    public static boolean checkIfEmail(String text) {
        return emailValidator.isValid(text);
    }

    public static String encryptPassword(String password) {
        return encoder.encode(password);
    }
}
