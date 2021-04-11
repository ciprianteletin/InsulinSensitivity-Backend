package com.insulin.model.form;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlucoseMandatory {
    @NotNull
    @Min(value = 0)
    private double fastingGlucose;
    @NotNull
    @Min(value = 0)
    private double glucoseThree;
    @NotNull
    @Min(value = 0)
    private double glucoseSix;
    @NotNull
    @Min(value = 0)
    private double glucoseOneTwenty;

    public void convert(String placeholder) {
        double conversionRate = 18.0;
        if (placeholder.equals("mg/dL")) {
            conversionRate = 1.0 / conversionRate;
        }
        fastingGlucose *= conversionRate;
        glucoseThree *= conversionRate;
        glucoseSix *= conversionRate;
        glucoseOneTwenty *= conversionRate;
    }
}
