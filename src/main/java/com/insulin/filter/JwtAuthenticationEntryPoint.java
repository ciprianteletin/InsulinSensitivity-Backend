package com.insulin.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insulin.shared.HttpResponse;
import com.insulin.utils.HttpResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

import static com.insulin.shared.SecurityConstants.FORBIDDEN_MESSAGE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This filter is called whenever the auth process fails, case when we want to send a custom message in case of error.
 * It is not mandatory to be implemented, used for customize error messages.
 * The default behaviour returns "Access Denied".
 */
@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        HttpResponse httpResponse = HttpResponseUtils.buildHttpResponse(HttpStatus.FORBIDDEN, FORBIDDEN_MESSAGE);
        response.setContentType(APPLICATION_JSON_VALUE); //json type, to pass our object
        response.setStatus(HttpStatus.FORBIDDEN.value()); //set the actual value of the request
        OutputStream out = response.getOutputStream(); //getting the output stream to output our custom response
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse); //mapping the stream with our response
        out.flush();
    }
}
