package com.insulin.model.form;

import com.insulin.formula.index.*;
import com.insulin.interfaces.CalculateIndex;
import com.insulin.interfaces.ExcelFormula;
import com.insulin.interfaces.FormulaMarker;

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
    public final Map<String, FormulaMarker> formulaCalculator = newHashMap();

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

    public ExcelFormula getExcelFormula(String index) {
        return formulaCalculator.get(index);
    }

    /**
     * Void method with a lot of function calls for every index.
     * Helps to populate the map with all formulas.
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
        addOgis();
    }

    private void addCederholm() {
        String key = "cederholm";
        FormulaMarker calculateIndex = new Cederholm();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addGutt() {
        String key = "gutt";
        FormulaMarker calculateIndex = new Gutt();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addMatsuda() {
        String key = "matsuda";
        FormulaMarker calculateIndex = new Matsuda();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addHoma() {
        String key = "homa";
        FormulaMarker calculateIndex = new Homa();
        formulaCalculator.put(key, calculateIndex);
        addHomaB();
        addLogHoma();
    }

    private void addHomaB() {
        String key = "homab";
        FormulaMarker calculateIndex = new HomaB();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addLogHoma() {
        String key = "loghoma";
        FormulaMarker calculateIndex = new LogHoma();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addQuicki() {
        String key = "quicki";
        FormulaMarker calculateIndex = new Quicki();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addRevised() {
        String key = "revised";
        FormulaMarker calculateIndex = new RevisedQuicki();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addAvignon() {
        String key = "avingon";
        FormulaMarker calculateIndex = new Avingon();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addBelfiore() {
        String key = "belfiore";
        FormulaMarker calculateIndex = new Belfiore();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addStumvoll() {
        addStumvollV1();
        addStumvollV2();
    }

    private void addStumvollV1() {
        String key = "stumvoll1";
        FormulaMarker calculateIndex = new StumvollV1();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addStumvollV2() {
        String key = "stumvoll2";
        FormulaMarker calculateIndex = new StumvollV2();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addMcAuley() {
        String key = "mcauley";
        FormulaMarker calculateIndex = new McAuley();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addSpise() {
        String key = "spise";
        FormulaMarker calculateIndex = new Spise();
        formulaCalculator.put(key, calculateIndex);
    }

    private void addOgis() {
        String key = "ogis";
        FormulaMarker calculateIndex = new Ogis();
        formulaCalculator.put(key, calculateIndex);
    }
}
