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
    public static final int FASTING_GLUCOSE_MG = 99;
    public static final int GLUCOSE_THREE_MG = 155;
    public static final int GLUCOSE_SIX_MG = 155;
    public static final int GLUCOSE_NINE_MG = 147;
    public static final int GLUCOSE_ONE_TWENTY_MG = 139;

    public static final double FASTING_GLUCOSE_MM = 5.5;
    public static final double GLUCOSE_THREE_MM = 8.61;
    public static final double GLUCOSE_SIX_MM = 8.61;
    public static final double GLUCOSE_NINE_MM = 8.16;
    public static final double GLUCOSE_ONE_TWENTY_MM = 7.72;

    public static final int FASTING_INSULIN_UI = 8;
    public static final int INSULIN_THREE_UI = 60;
    public static final int INSULIN_SIX_UI = 60;
    public static final int INSULIN_NINE_UI = 50;
    public static final int INSULIN_ONE_TWENTY_UI = 40;

    public static final int FASTING_INSULIN_PM = 48;
    public static final int INSULIN_THREE_PM = 360;
    public static final int INSULIN_SIX_PM = 360;
    public static final int INSULIN_NINE_PM = 300;
    public static final int INSULIN_ONE_TWENTY_PM = 240;
}
