package com.insulin.model.form;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsulinMandatory {
    @NotNull
    @Min(value = 0)
    private double fastingInsulin;
    @NotNull
    @Min(value = 0)
    private double insulinThree;
    @NotNull
    @Min(value = 0)
    private double insulinSix;
    @NotNull
    @Min(value = 0)
    private double insulinOneTwenty;

    public void convert(String placeholder) {
        double conversionRate = 6.0;
        if (placeholder.equals("pmol/L")) {
            conversionRate = 1.0 / conversionRate;
        }
        fastingInsulin *= conversionRate;
        insulinThree *= conversionRate;
        insulinSix *= conversionRate;
        insulinOneTwenty *= conversionRate;
    }
}
