package com.insulin.excel.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Class to build the header for the general information page. As every new Excel doc. will have this page
 * and the headers won't change, the decision of creating a Singleton instance of the list was made.
 * <p>
 * If the list is null for the first time when we want to retrieve the information, it will be created and
 * populated with information. Afterwards, the same list will be returned every time.
 */
public final class InformationPageUtils {
    private static List<String> dataNames;
    public static final int GLUCOSE_START = 4;
    public static final int INSULIN_START = 10;
    public static final int OPTIONAL_START = 16;

    private InformationPageUtils() {

    }

    public static List<String> getDataNamesList() {
        if (dataNames == null) {
            dataNames = new LinkedList<>();
            addUserHeader();
            addGlucoseHeaders();
            addInsulinHeaders();
            addOptionalHeaders();
        }
        return dataNames;
    }

    private static void addUserHeader() {
        dataNames.add("Gender");
        dataNames.add("Age");
    }

    private static void addGlucoseHeaders() {
        dataNames.add("Unit of measurement");
        dataNames.add("Fasting glucose");
        dataNames.add("Glucose 30 min");
        dataNames.add("Glucose 60 min");
        dataNames.add("Glucose 90 min");
        dataNames.add("Glucose 120 min");
    }

    private static void addInsulinHeaders() {
        dataNames.add("Unit of measurement");
        dataNames.add("Fasting Insulin");
        dataNames.add("Insulin 30 min");
        dataNames.add("Insulin 60 min");
        dataNames.add("Insulin 90 min");
        dataNames.add("Insulin 120 min");
    }

    private static void addOptionalHeaders() {
        dataNames.add("Weight");
        dataNames.add("Height");
        dataNames.add("HDL");
        dataNames.add("NEFA");
        dataNames.add("Triglyceride");
        dataNames.add("Thyroglobulin");
    }
}
