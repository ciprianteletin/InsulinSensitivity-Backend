package com.insulin.utils;

import com.insulin.metadata.MetaInformation;
import org.apache.commons.lang3.RandomStringUtils;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Utility class with reusable methods related to authentication. Since the AuthController has as main scope to treat request of any kind,
 * adding new functionality would break Single Resp. Principle, hence creating a new class.
 */
public class AuthenticationUtils {

    public static String getDeviceDetails(String userAgent) {
        StringBuilder deviceDetails = new StringBuilder();
        Client client = new Parser().parse(userAgent);
        if (Objects.nonNull(client)) {
            deviceDetails.append(client.userAgent.family) //
                    .append(" ").append(client.userAgent.major) //
                    .append(".").append(client.userAgent.minor) //
                    .append("-").append(client.os.family) //
                    .append(" ").append(client.os.major) //
                    .append(".").append(client.os.minor);
        }
        return deviceDetails.toString();
    }

    public static MetaInformation createMetaDataInformation(Long userId, HttpServletRequest request) {
        String randomToken = RandomStringUtils.randomAlphanumeric(25);
        String userAgent = request.getHeader("user-agent");
        String deviceDetails = getDeviceDetails(userAgent);

        return MetaInformation.builder() //
                .userId(userId) //
                .refreshToken(randomToken) //
                .deviceInformation(deviceDetails) //
                .build();
    }
}
