package com.insulin.model.form;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Wrapper class which contains all necessary details for index calculation.
 * Inside this class resides data from the user directly.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MandatoryIndexInformation {
    @Size(min = 1, max = 1)
    private String gender;
    private String fullName;
    @Min(value = 16)
    private Integer age;
    @NonNull
    private List<String> selectedIndexes;
    @Valid
    private GlucoseMandatory glucoseMandatory;
    @Valid
    private InsulinMandatory insulinMandatory;
    @Valid
    private OptionalIndexInformation optionalInformation;
}
