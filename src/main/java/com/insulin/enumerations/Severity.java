package com.insulin.enumerations;

public enum Severity {
    DEFAULT("-"), HEALTHY("Healthy"), PREDIABETES("Prediabetes"), INSULIN_RESISTANCE("Insulin Resistance"), DIABETES("Diabetes");

    private final String name;

    Severity(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
