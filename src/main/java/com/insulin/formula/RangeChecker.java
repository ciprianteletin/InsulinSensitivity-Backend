package com.insulin.formula;

/**
 * Convenience methods to check the range for several index formulas.
 * The alternative is to use the checks directly in if statements,
 * but this alternative is cleaner.
 */
public final class RangeChecker {
    private RangeChecker() {

    }

    public static boolean checkInBetween(double lower, double upper, double result) {
        return lower < result && upper > result;
    }

    public static boolean checkUpperBound(double upper, double result) {
        return upper > result;
    }

    public static boolean checkLowerBound(double lower, double result) {
        return lower < result;
    }
}
