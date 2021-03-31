package com.insulin.utils;

import com.insulin.shared.HttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

/**
 * Creates a new ResponseEntity of HttpResponse or a HttpResponse based on the status passed and also on the message of the error.
 * The httpResponse object will be what the user will see.
 */
public final class HttpResponseUtils {

    private HttpResponseUtils(){}

    public static ResponseEntity<HttpResponse> buildHttpResponseEntity(HttpStatus status, String message) {
        HttpResponse httpResponse = buildHttpResponse(status, message);
        return new ResponseEntity<>(httpResponse, status);
    }

    public static ResponseEntity<HttpResponse> buildHttpResponseWithHeader(HttpStatus status, String message, HttpHeaders headers) {
        HttpResponse httpResponse = buildHttpResponse(status, message);
        return new ResponseEntity<>(httpResponse, headers, status);
    }

    public static HttpResponse buildHttpResponse(HttpStatus status, String message) {
        return HttpResponse.builder() //
                .httpStatus(status) //
                .httpStatusCode(status.value()) //
                .message(message) //
                .reason(status.getReasonPhrase()) //
                .timeStamp(LocalDate.now()) //
                .build();
    }
}
