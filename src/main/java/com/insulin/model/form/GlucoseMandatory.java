package com.insulin.model.form;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.insulin.formula.ValueConverter.GLUCOSE_CONVERT;

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
    @NonNull
    private String glucosePlaceholder;

    public void convert() {
        double conversionRate = GLUCOSE_CONVERT;
        if (this.glucosePlaceholder.equals("mg/dL")) {
            conversionRate = 1.0 / conversionRate;
        }
        fastingGlucose *= conversionRate;
        glucoseThree *= conversionRate;
        glucoseSix *= conversionRate;
        glucoseOneTwenty *= conversionRate;
    }
}
