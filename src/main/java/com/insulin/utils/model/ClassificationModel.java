package com.insulin.utils.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassificationModel {
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
    private Double cholHDLRatio;
    @NonNull
    private Double systolic;
    @NonNull
    private Double diastolic;
    @NonNull
    private Double weight;
    @NonNull
    private Double height;
    @NonNull
    private Double waist;
    @NonNull
    private Double hip;
    private Double waistHipRatio;
    private Double bmi;

    @NonNull
    private String placeholder;
}
