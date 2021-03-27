package com.insulin.utils.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.insulin.validation.BirthDay;
import com.insulin.validation.Phone;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;


/**
 * Complete user represents the user with all the details provided in order to register a user into the database.
 * It is the JSON representation of the User and UserDetails class. It excludes the fields that are set in the code.
 * It's using Lombok for default method creation.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompleteUser {
    @JsonProperty
    @NotNull
    @Size(min = 6, max = 30)
    private String username;
    @JsonProperty
    @NotNull
    @Size(min = 8)
    private String password;
    @JsonProperty
    @NotNull
    private String role;
    @JsonProperty
    @NotNull
    @Size(min = 3, max = 20)
    private String firstName;
    @JsonProperty
    @NotNull
    @Size(min = 3, max = 20)
    private String lastName;
    @JsonProperty
    @NotNull
    @Email
    private String email;
    @JsonProperty
    @NotNull
    @Phone
    private String phoneNr;
    @JsonProperty
    @NotNull
    @BirthDay
    private String birthDay;
    @JsonProperty
    @NotNull
    private Character gender;
}
