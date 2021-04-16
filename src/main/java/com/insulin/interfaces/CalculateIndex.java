package com.insulin.interfaces;

import com.insulin.model.form.IndexResult;
import com.insulin.model.form.MandatoryInsulinInformation;

/**
 * Functional Interface used for the creation of the needed formulas for
 * calculating diverse indexes. For each index, a lambda function will
 * be created.
 *
 * For simple usage, all formulas will be kept in a map, so that
 * we can access them based on the picked indexes from GUI.
 */
public interface CalculateIndex {
    IndexResult calculate(MandatoryInsulinInformation mandatoryInformation);
}
