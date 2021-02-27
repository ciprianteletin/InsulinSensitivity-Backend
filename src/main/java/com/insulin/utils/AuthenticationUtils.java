package com.insulin.utils;

import com.insulin.metadata.MetaInformation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.web.context.request.RequestContextHolder;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.insulin.shared.SecurityConstants.REFRESH_EXPIRATION_TIME;
import static com.insulin.utils.RequestUtils.getRemoteIP;

/**
 * Utility class with reusable methods related to authentication. Since the AuthController has as main scope to treat request of any kind,
 * adding new functionality would break Single Resp. Principle, hence creating a new class.
 */
public class AuthenticationUtils {
    private static final EmailValidator emailValidator;

    static {
        emailValidator = EmailValidator.getInstance();
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
        deviceDetails = deviceDetails + " " + ip;

        return MetaInformation.builder() //
                .userId(userId) //
                .refreshToken(randomToken) //
                .expirationTime(REFRESH_EXPIRATION_TIME) // 7 days Expiration time, match the cookie duration)
                .deviceInformation(deviceDetails) //
                .build();
    }

    public static boolean checkIfEmail(String text) {
        return emailValidator.isValid(text);
    }
}
