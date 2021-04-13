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
        addQuicki();
        addRevised();
        addAvignon();
        addBelfiore();
        addStumvoll();
        addMcAuley();
        addSpise();
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
        addHomaB();
        addLogHoma();
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

    private void addAvignon() {
        String key = "avignon";
        CalculateIndex calculateIndex = new Avignon();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addBelfiore() {
        String key = "belfiore";
        CalculateIndex calculateIndex = new Belfiore();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addStumvoll() {
        addStumvollV1();
        addStumvollV2();
    }

    private void addStumvollV1() {
        String key = "stumvoll1";
        CalculateIndex calculateIndex = new StumvollV1();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addStumvollV2() {
        String key = "stumvoll2";
        CalculateIndex calculateIndex = new StumvollV2();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addMcAuley() {
        String key = "mcauley";
        CalculateIndex calculateIndex = new McAuley();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addSpise() {
        String key = "spise";
        CalculateIndex calculateIndex = new Spise();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addOgis() {
        String key = "ogis";
        CalculateIndex calculateIndex = new Ogis();
        formulaCalculator.put(key, calculateIndex);
    }
}
