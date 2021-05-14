package com.insulin.utils.model;

import com.insulin.validation.Phone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactModel {
    @NotNull
    private String name;
    @Email
    @NotNull
    private String email;
    @Phone
    @NotNull
    private String phone;
    @NotNull
    @Size(min = 5)
    private String message;
}
