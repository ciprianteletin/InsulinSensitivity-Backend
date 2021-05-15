package com.insulin.shared.constants;

public final class NumericConstants {
    public static final int TEN_FOUR = 10_000;
    public static final int TEN_EIGHT = 100_000_000;
    public static final double BALANCE_AVIGNON = 0.137;

    /**
     * Normal values for Glucose and Insulin.
     * The values are used from https://sites.google.com/a/shindeclinic.com/insulin-resistance-calculator/insulin-resistance-calculator
     * Those are the values displayed inside the graph
     *
     * Constants used also for belfiore index
     */
    public static final double FASTING_GLUCOSE_MM = 5.5;
    public static final double GLUCOSE_SIX_MM = 8.61;
    public static final double GLUCOSE_ONE_TWENTY_MM = 7.72;

    public static final int FASTING_INSULIN_UI = 8;
    public static final int INSULIN_SIX_UI = 60;
    public static final int INSULIN_ONE_TWENTY_UI = 40;
}
