package com.insulin.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Complete user represents the user with all the details provided in order to register a user into the database.
 * It is the JSON representation of the User and UserDetails class. It excludes the fields that are set in the code.
 * It's using Lombok for default method creation.
 */
@Builder
@NoArgsConstructor
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
