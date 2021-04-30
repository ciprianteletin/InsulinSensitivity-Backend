package com.insulin.excel.utils;

import java.util.HashMap;
import java.util.Map;

import static com.insulin.excel.utils.ExcelCommons.infoSheetName;
import static com.insulin.shared.constants.IndexDataConstants.*;
import static com.insulin.shared.constants.NumericConstants.*;

public class FormulaExcelUtils {
    private static final Map<String, String> referenceMap;

    static {
        referenceMap = new HashMap<>();
        populateRefMap();
    }

    private FormulaExcelUtils() {
    }

    private static int getActualInfoIdGlucose(int infoId, String placeholder) {
        int actualIndex = 3 * infoId;
        if (placeholder.equals("mg/dL")) {
            --actualIndex;
        }
        return actualIndex;
    }

    private static int getActualInfoIdInsulin(int infoId, String placeholder) {
        int actualIndex = 3 * infoId;
        if (placeholder.equals("pmol/L")) {
            return actualIndex;
        }
        return actualIndex - 1;
    }

    public static String getGlucose(int infoId, String placeholder, String field) {
        int actualIndex = getActualInfoIdGlucose(infoId, placeholder);
        String column = referenceMap.get(field);
        return infoSheetName + "!" + column + actualIndex;
    }

    public static String getInsulin(int infoId, String placeholder, String field) {
        int actualIndex = getActualInfoIdInsulin(infoId, placeholder);
        String column = referenceMap.get(field);
        return infoSheetName + "!" + column + actualIndex;
    }

    public static String getAge(int infoId) {
        int actualIndex = 3 * infoId - 1;
        String column = referenceMap.get(AGE);
        return infoSheetName + "!" + column + actualIndex;
    }

    /**
     * Works for height, weight, Thyroglobulin.
     */
    public static String getOptionalNoPlaceholder(int infoId, String field) {
        String column = referenceMap.get(field);
        int actualIndex = infoId * 3;
        return infoSheetName + "!" + column + actualIndex;
    }

    /**
     * Works for HDL, Nefa, Triglyceride
     */
    public static String getOptionalWithPlaceholder(int infoId, String placeholder, String field) {
        int actualIndex = getActualInfoIdGlucose(infoId, placeholder);
        String column = referenceMap.get(field);
        return infoSheetName + "!" + column + actualIndex;
    }

    public static String getGlucoseMeanIncomplete(int infoId, String placeholder) {
        int actualIndex = getActualInfoIdGlucose(infoId, placeholder);
        String start = infoSheetName + "!" + referenceMap.get(FASTING_GLUCOSE) + actualIndex;
        String firstEnd = infoSheetName + "!" + referenceMap.get(GLUCOSE_SIX) + actualIndex;

        String glucoseOne = infoSheetName + "!" + referenceMap.get(GLUCOSE_ONE_TWENTY) + actualIndex;
        return "AVERAGE(" + start + ":" + firstEnd + "," + glucoseOne + ")";
    }

    public static String getGlucoseSubject(int infoId, String placeholder) {
        String fastingGlucose = getGlucose(infoId, placeholder, FASTING_GLUCOSE);
        String glucoseSix = getGlucose(infoId, placeholder, GLUCOSE_SIX);
        String glucoseOne = getGlucose(infoId, placeholder, GLUCOSE_ONE_TWENTY);

        return "(0.5 * " + fastingGlucose + " + " + glucoseSix + " + " + glucoseOne + ")";
    }

    public static String getGlucoseMean(int infoId, String placeholder) {
        int actualIndex = getActualInfoIdGlucose(infoId, placeholder);
        String start = infoSheetName + "!" + referenceMap.get(FASTING_GLUCOSE) + actualIndex;
        String end = infoSheetName + "!" + referenceMap.get(GLUCOSE_ONE_TWENTY) + actualIndex;
        return "AVERAGE(" + start + ":" + end + ")";
    }

    public static String getInsulinMeanIncomplete(int infoId, String placeholder) {
        int actualIndex = getActualInfoIdInsulin(infoId, placeholder);
        String start = infoSheetName + "!" + referenceMap.get(FASTING_INSULIN) + actualIndex;
        String firstEnd = infoSheetName + "!" + referenceMap.get(INSULIN_SIX) + actualIndex;

        String insulinOne = infoSheetName + "!" + referenceMap.get(INSULIN_ONE_TWENTY) + actualIndex;
        return "AVERAGE(" + start + ":" + firstEnd + "," + insulinOne + ")";
    }

    public static String getInsulinSubject(int infoId, String placeholder) {
        String fastingInsulin = getInsulin(infoId, placeholder, FASTING_INSULIN);
        String insulinSix = getInsulin(infoId, placeholder, INSULIN_SIX);
        String insulinOne = getInsulin(infoId, placeholder, INSULIN_ONE_TWENTY);

        return "(0.5 * " + fastingInsulin + " + " + insulinSix + " + " + insulinOne + ")";
    }

    public static String getInsulinMean(int infoId, String placeholder) {
        int actualIndex = getActualInfoIdInsulin(infoId, placeholder);
        String start = infoSheetName + "!" + referenceMap.get(FASTING_INSULIN) + actualIndex;
        String end = infoSheetName + "!" + referenceMap.get(INSULIN_ONE_TWENTY) + actualIndex;

        return "AVERAGE(" + start + ":" + end + ")";
    }

    public static String getBMI(int infoId) {
        String weight = getOptionalNoPlaceholder(infoId, WEIGHT);
        String height = getOptionalNoPlaceholder(infoId, HEIGHT) + " / 100";

        return weight + " / " + "POWER(" + height + ", 2)";
    }

    public static String getLog(String value) {
        return "LN(" + value + ")";
    }

    public static String getSqrt(String value) {
        return "SQRT(" + value + ")";
    }

    public static String getExp(String value) {
        return "EXP(" + value + ")";
    }

    public static String getPower(String value, String power) {
        return "POWER(" + value + ", " + power + ")";
    }

    public static String getGlucoseNormal() {
        double val = 0.5 * FASTING_GLUCOSE_MM + GLUCOSE_SIX_MM + GLUCOSE_ONE_TWENTY_MM;
        return val + "";
    }

    public static String getInsulinNormal() {
        double val = 0.5 * FASTING_INSULIN_UI + INSULIN_SIX_UI + INSULIN_ONE_TWENTY_UI;
        return val + "";
    }

    private static void populateRefMap() {
        referenceMap.put(AGE, "D");
        referenceMap.put(FASTING_GLUCOSE, "F");
        referenceMap.put(GLUCOSE_THREE, "G");
        referenceMap.put(GLUCOSE_SIX, "H");
        referenceMap.put(GLUCOSE_NINE, "I");
        referenceMap.put(GLUCOSE_ONE_TWENTY, "J");

        referenceMap.put(FASTING_INSULIN, "L");
        referenceMap.put(INSULIN_THREE, "M");
        referenceMap.put(INSULIN_SIX, "N");
        referenceMap.put(INSULIN_NINE, "O");
        referenceMap.put(INSULIN_ONE_TWENTY, "P");

        referenceMap.put(WEIGHT, "Q");
        referenceMap.put(HEIGHT, "R");
        referenceMap.put(HDL, "S");
        referenceMap.put(NEFA, "T");
        referenceMap.put(TRIGLYCERIDE, "U");
        referenceMap.put(THYROGLOBULIN, "V");
    }
}
