package com.insulin.utils.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.insulin.validation.Phone;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * Class used for updating the data provided from settings page.
 * Used for update of username, email and phoneNr.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasicUserInfo {
    @JsonProperty
    @NotNull
    @Size(min = 6, max = 30)
    private String username;
    @JsonProperty
    @NotNull
    @Email
    private String email;
    @JsonProperty
    @NotNull
    @Phone
    private String phoneNr;
}
