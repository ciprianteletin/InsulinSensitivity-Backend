package com.insulin.model.form;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MandatoryInsulinInformation {
    @Size(min = 6, max = 30)
    // needed to register data for a logged user
    private String username;
    // Not required if we have a username
    @Size(min = 1, max = 1)
    private String gender;
    private String fullName;
    @Min(value = 16)
    private Integer age;
    @NonNull
    private List<String> selectedIndexes;
    @Valid
    private Placeholders placeholders;
    @Valid
    private GlucoseMandatory glucoseMandatory;
    @Valid
    private InsulinMandatory insulinMandatory;
    @Valid
    private OptionalInsulinInformation optionalInformation;
}
