package com.insulin.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public final class RequestUtils {

    private RequestUtils(){}

    private static final String[] IP_HEADER_NAMES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static String getRemoteIP(RequestAttributes requestAttributes) {
        if (requestAttributes == null) {
            return "0.0.0.0";
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String ip = Arrays.stream(IP_HEADER_NAMES)
                .map(request::getHeader)
                .filter(h -> h != null && h.length() != 0 && !h.equalsIgnoreCase("unknown"))
                .map(h -> h.split(",")[0])
                .reduce("", (h1, h2) -> h1 + ":" + h2);
        return ip + request.getRemoteAddr();
    }
}
