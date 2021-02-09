package com.insulin.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insulin.shared.HttpResponse;
import com.insulin.utils.HttpResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

import static com.insulin.shared.SecurityConstants.ACCESS_DENIED_MESSAGE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Handler called every time an user encounters an Unauthorized exception (if he lacks permission)
 * We override the default behaviour which throw a generic message in a less nicer format.
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        HttpResponse httpResponse = HttpResponseUtils.buildHttpResponse(HttpStatus.UNAUTHORIZED, ACCESS_DENIED_MESSAGE);
        response.setContentType(APPLICATION_JSON_VALUE); //json type, to pass our object
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); //set the actual value of the request
        OutputStream out = response.getOutputStream(); //getting the output stream to output our custom response
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse); //mapping the stream with our response
        out.flush();
    }
}
