package com.insulin.model.form;

import com.insulin.formula.Cederholm;
import com.insulin.functional.CalculateIndex;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Objects.isNull;

/**
 * Singleton class for index formula, used to store the index name and the corresponding
 * formula in a map, using the structure name -> formula.
 * All formulas will be stored here, no matter if all of them are used or not.
 */
public final class Formula {
    private static Formula formula;
    public final Map<String, CalculateIndex> formulaCalculator = newHashMap();

    private Formula() {
        prepopulateMap();
    }

    public static Formula getInstance() {
        if (isNull(formula)) {
            formula = new Formula();
        }
        return formula;
    }

    public CalculateIndex getFormula(String index) {
        return formulaCalculator.get(index);
    }

    /**
     * Void method with a lot of function calls for every index.
     * Helps to populate the map with desired formulas.
     */
    private void prepopulateMap() {
        addCederholm();
    }

    private void addCederholm() {
        String key = "cederholm";
        CalculateIndex calculateIndex = new Cederholm();
        formulaCalculator.put(key, calculateIndex);
    }
}
