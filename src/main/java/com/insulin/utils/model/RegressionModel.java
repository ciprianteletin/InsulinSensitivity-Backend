package com.insulin.utils.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegressionModel {
    @NonNull
    private Integer age;
    @NonNull
    private Character gender;
    @NonNull
    private Double glucose;
    @NonNull
    private Double cholesterol;
    @NonNull
    private Double hdl;
    @NonNull
    private Double ldl;
    @NonNull
    private Double ltg;
    @NonNull
    private Double bloodPressure;
    private Integer tch;
    @NonNull
    private Double bmi;

    @NonNull
    private String placeholder;
}
