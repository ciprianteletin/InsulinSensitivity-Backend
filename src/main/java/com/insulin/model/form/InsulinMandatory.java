package com.insulin.model.form;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.insulin.formula.ValueConverter.INSULIN_CONVERT;

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
    @NotNull
    private String insulinPlaceholder;

    public void convert() {
        double conversionRate = INSULIN_CONVERT;
        if (this.insulinPlaceholder.equals("pmol/L")) {
            conversionRate = 1.0 / conversionRate;
        }
        fastingInsulin *= conversionRate;
        insulinThree *= conversionRate;
        insulinSix *= conversionRate;
        insulinOneTwenty *= conversionRate;
    }
}
