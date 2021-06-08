package com.insulin.model.form;

import com.insulin.validation.InsulinPlaceholder;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.insulin.formula.ValueConverter.INSULIN_CONVERT;

@Entity
@Table(name = "insulin_mandatory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsulinMandatory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insulin_generator")
    @SequenceGenerator(name="insulin_generator", sequenceName = "insulin_seq")
    @Column(nullable = false, updatable = false)
    private Long id;
    @NotNull
    @Min(value = 0)
    @Max(value = 300)
    @Column(name = "fasting_insulin", updatable = false)
    private double fastingInsulin;
    @NotNull
    @Min(value = 0)
    @Max(value = 300)
    @Column(name = "insulin_three", updatable = false)
    private double insulinThree;
    @NotNull
    @Min(value = 0)
    @Max(value = 300)
    @Column(name = "insulin_six", updatable = false)
    private double insulinSix;
    @NotNull
    @Min(value = 0)
    @Max(value = 300)
    @Column(name = "insulin_onetwenty", updatable = false)
    private double insulinOneTwenty;
    @NotNull
    @InsulinPlaceholder
    @Column(name = "insulin_placeholder", updatable = false)
    private String insulinPlaceholder;

    public void convert() {
        double conversionRate = INSULIN_CONVERT;
        if (this.insulinPlaceholder.equals("pmol/L")) {
            conversionRate = 1.0 / conversionRate;
            this.insulinPlaceholder = "μIU/mL";
        } else {
            this.insulinPlaceholder = "pmol/L";
        }
        fastingInsulin *= conversionRate;
        insulinThree *= conversionRate;
        insulinSix *= conversionRate;
        insulinOneTwenty *= conversionRate;
    }
}
