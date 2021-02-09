package com.insulin.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Complete user represents the user with all the details provided in order to register a user into the database.
 * It is the JSON representation of the User and UserDetails class. It excludes the fields that are set in the code.
 * It's using Lombok for default method creation.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompleteUser {
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;
    @JsonProperty
    private String role;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String email;
    @JsonProperty
    private String phoneNr;
}
