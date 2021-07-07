package com.insulin.model.form;

import com.insulin.validation.GlucosePlaceholder;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.insulin.formula.ValueConverter.GLUCOSE_CONVERT;

/**
 * Entity which is mapped to a table with the same name and attributes.
 * Container for glucose related details.
 */
@Entity
@Table(name = "glucose_mandatory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlucoseMandatory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "glucose_generator")
    @SequenceGenerator(name="glucose_generator", sequenceName = "glucose_seq")
    @Column(nullable = false, updatable = false)
    private Long id;
    @NotNull
    @Min(value = 0)
    @Max(value = 300)
    @Column(name = "fasting_glucose", updatable = false)
    private double fastingGlucose;
    @NotNull
    @Min(value = 0)
    @Max(value = 300)
    @Column(name = "glucose_three", updatable = false)
    private double glucoseThree;
    @NotNull
    @Min(value = 0)
    @Max(value = 300)
    @Column(name = "glucose_six", updatable = false)
    private double glucoseSix;
    @NotNull
    @Min(value = 0)
    @Max(value = 300)
    @Column(name = "glucose_onetwenty", updatable = false)
    private double glucoseOneTwenty;
    @NonNull
    @GlucosePlaceholder
    @Column(name = "glucose_placeholder", updatable = false)
    private String glucosePlaceholder;

    public void convert() {
        double conversionRate = GLUCOSE_CONVERT;
        if (this.glucosePlaceholder.equals("mg/dL")) {
            conversionRate = 1.0 / conversionRate;
            this.glucosePlaceholder = "mmol/L";
        } else {
            this.glucosePlaceholder = "mg/dL";
        }
        fastingGlucose *= conversionRate;
        glucoseThree *= conversionRate;
        glucoseSix *= conversionRate;
        glucoseOneTwenty *= conversionRate;
    }
}
