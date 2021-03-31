package com.insulin.utils.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * Class to map the model send from the client, used in the process in updating the account password.
 * We provide the username and both passwords to check and match if the user is actually the one
 * logged in.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserPasswordInfo {
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
    @Size(min = 8)
    private String newPassword;
}
