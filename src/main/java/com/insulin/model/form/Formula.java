package com.insulin.model.form;

import com.insulin.formula.*;
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
        addGutt();
        addMatsuda();
        addHoma();
        addHomaB();
        addLogHoma();
        addQuicki();
        addRevised();
    }

    private void addCederholm() {
        String key = "cederholm";
        CalculateIndex calculateIndex = new Cederholm();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addGutt() {
        String key = "gutt";
        CalculateIndex calculateIndex = new Gutt();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addMatsuda() {
        String key = "matsuda";
        CalculateIndex calculateIndex = new Matsuda();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addHoma() {
        String key = "homa";
        CalculateIndex calculateIndex = new Homa();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addHomaB() {
        String key = "homab";
        CalculateIndex calculateIndex = new HomaB();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addLogHoma() {
        String key = "loghoma";
        CalculateIndex calculateIndex = new LogHoma();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addQuicki() {
        String key = "quicki";
        CalculateIndex calculateIndex = new Quicki();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addRevised() {
        String key = "revised";
        CalculateIndex calculateIndex = new RevisedQuicki();
        formulaCalculator.put(key, calculateIndex);
    }
}
