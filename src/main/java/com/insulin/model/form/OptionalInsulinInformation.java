package com.insulin.model.form;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.insulin.formula.ValueConverter.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionalInsulinInformation {
    @Min(value = 120)
    @Max(value = 250)
    private Double height;
    @Min(value = 40)
    @Max(value = 300)
    private Double weight;
    @Min(value = 0)
    private Double nefa;
    @Min(value = 0)
    private Double hdl;
    @Min(value = 0)
    private Double thyroglobulin;
    @Min(value = 0)
    private Double triglyceride;

    public void convert(String actual) {
        // From mg/dL to mmol
        double nefaConvert = NEFA_CONVERT;
        double hdlConvert = 1. / HDL_CONVERT;
        double triglycerideConvert = TRYGLICERIDE_CONVERT;

        if (actual.equals("mmol/L")) {
            nefaConvert = 1. / nefaConvert;
            hdlConvert = 1. / hdlConvert;
            triglycerideConvert = 1. / triglycerideConvert;
        }
        nefa = convertNotNull(nefa, nefaConvert);
        hdl = convertNotNull(hdl, hdlConvert);
        triglyceride = convertNotNull(triglyceride, triglycerideConvert);
    }

    private Double convertNotNull(Double value, double converter) {
        if (value == null) {
            return null;
        }
        return value * converter;
    }
}
