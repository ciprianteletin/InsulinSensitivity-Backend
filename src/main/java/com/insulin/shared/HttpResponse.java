package com.insulin.shared;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

/**
 * Standard class to define a custom error message for the user, passing as many information as needed.
 * Implements @Builder annotation, for creating objects in a nicer way. @Builder is coming from Lombok project.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class HttpResponse {
    @JsonProperty
    private int httpStatusCode;
    @JsonProperty
    private HttpStatus httpStatus;
    @JsonProperty
    private String reason;
    @JsonProperty
    private String message;
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate timeStamp;
}
