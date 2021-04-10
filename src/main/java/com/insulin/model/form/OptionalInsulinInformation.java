package com.insulin.model.form;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionalInsulinInformation {
    @Min(value = 120)
    @Max(value = 240)
    private Integer height;
    @Min(value = 40)
    @Max(value = 300)
    private Integer weight;
    @Min(value = 0)
    private Double nefa;
    @Min(value = 0)
    private Double hdl;
    @Min(value = 0)
    private Double thyroglobulin;
    @Min(value = 0)
    private Double triglyceride;

}
