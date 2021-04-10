package com.insulin.model.form;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MandatoryInsulinInformation {
    @Size(min = 6, max = 30)
    private String username;
    // Not required if we have a username
    private Character sex;
    private String fullName;
    @Min(value = 16)
    private Integer age;
    @Valid
    private Placeholders placeholders;
    @Valid
    private GlucoseMandatory glucoseMandatory;
    @Valid
    private InsulinMandatory insulinMandatory;
    @Valid
    private OptionalInsulinInformation optionalInformation;
}
