package com.insulin.formula.result;

import com.insulin.interfaces.Interpreter;
import com.insulin.model.form.GlucoseMandatory;

import java.util.List;
import java.util.Optional;

/**
 * Class which will keep all interpreters in one place, so that every
 * user can check their input against the interpreters list.
 */
public final class Analyzer {
    private static Analyzer analyzer;
    private List<Interpreter> interpreters;

    private Analyzer() {
        populateInterpreters();
    }

    private void populateInterpreters() {
        this.interpreters = List.of(
                new Diabetes(), new DiabetesNormalPG(), new DiabetesNormalFasting(),
                new DiabetesImpairedFasting(), new DiabetesImpairedGlucose(),
                new ImpairedFastingGlucose(), new ImpairedFastingPost(),
                new ImpairedGlucoseTolerance(), new InsulinResistanceThree(),
                new InsulinResistanceSix(), new InsulinResistanceBoth()
        );
    }

    public static Analyzer getInstance() {
        if (analyzer == null) {
            analyzer = new Analyzer();
        }
        return analyzer;
    }

    public String filterGlucoseMandatoryResult(GlucoseMandatory glucoseMandatory) {
        String result = "Healthy";
        Optional<Interpreter> interpreter = interpreters.stream()
                .filter(in -> in.interpret(glucoseMandatory))
                .findAny();
        if (interpreter.isPresent()) {
            result = interpreter.get().getName();
        }
        return result;
    }
}
