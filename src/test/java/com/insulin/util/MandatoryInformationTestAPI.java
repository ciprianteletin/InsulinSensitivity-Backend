package com.insulin.util;

import com.insulin.model.form.GlucoseMandatory;
import com.insulin.model.form.InsulinMandatory;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.model.form.OptionalIndexInformation;

import java.util.ArrayList;

/**
 * Testing api to obtain the desired objects and to test different use cases.
 */
public class MandatoryInformationTestAPI {
    private GlucoseMandatory glucoseMandatory;
    private InsulinMandatory insulinMandatory;
    private final OptionalIndexInformation optionalInformation;

    private MandatoryInformationTestAPI() {
        this.optionalInformation = new OptionalIndexInformation();
    }

    public static MandatoryInformationTestAPI buildMandatoryInformation() {
        return new MandatoryInformationTestAPI();
    }

    public MandatoryInformationTestAPI withDefaultMgGlucose() {
        this.glucoseMandatory = GlucoseMandatory.builder()
                .glucosePlaceholder("mg/dL")
                .fastingGlucose(100)
                .glucoseThree(120)
                .glucoseSix(130)
                .glucoseOneTwenty(110)
                .build();
        return this;
    }

    public MandatoryInformationTestAPI withDefaultMmolGlucose() {
        this.glucoseMandatory = GlucoseMandatory.builder()
                .glucosePlaceholder("mmol/L")
                .fastingGlucose(100. / 18)
                .glucoseThree(120. / 18)
                .glucoseSix(130. / 18)
                .glucoseOneTwenty(110. / 18)
                .build();
        return this;
    }

    public MandatoryInformationTestAPI glucoseWithValues(String placeholder, Double fast, Double three, Double six, Double oneTwenty) {
        this.glucoseMandatory = GlucoseMandatory.builder()
                .glucosePlaceholder(placeholder)
                .fastingGlucose(fast)
                .glucoseThree(three)
                .glucoseSix(six)
                .glucoseOneTwenty(oneTwenty)
                .build();
        return this;
    }

    public MandatoryInformationTestAPI withDefaultUiInsulin() {
        this.insulinMandatory = InsulinMandatory.builder()
                .insulinPlaceholder("μIU/mL")
                .fastingInsulin(5)
                .insulinThree(20)
                .insulinSix(40)
                .insulinOneTwenty(30)
                .build();
        return this;
    }

    public MandatoryInformationTestAPI withDefaultPmolInsulin() {
        this.insulinMandatory = InsulinMandatory.builder()
                .insulinPlaceholder("pmol/L")
                .fastingInsulin(30)
                .insulinThree(120)
                .insulinSix(240)
                .insulinOneTwenty(180)
                .build();
        return this;
    }

    public MandatoryInformationTestAPI insulinWithValues(String placeholder, Double fast, Double three, Double six, Double oneTwenty) {
        this.insulinMandatory = InsulinMandatory.builder()
                .insulinPlaceholder(placeholder)
                .fastingInsulin(fast)
                .insulinThree(three)
                .insulinSix(six)
                .insulinOneTwenty(oneTwenty)
                .build();
        return this;
    }

    public MandatoryInformationTestAPI withHeight(Double height) {
        this.optionalInformation.setHeight(height);
        return this;
    }

    public MandatoryInformationTestAPI withWeight(Double weight) {
        this.optionalInformation.setWeight(weight);
        return this;
    }

    public MandatoryInformationTestAPI withNefa(Double nefa) {
        this.optionalInformation.setNefa(nefa);
        return this;
    }

    public MandatoryInformationTestAPI withHDL(Double hdl) {
        this.optionalInformation.setHdl(hdl);
        return this;
    }

    public MandatoryInformationTestAPI withThyroglobulin(Double thyro) {
        this.optionalInformation.setThyroglobulin(thyro);
        return this;
    }

    public MandatoryInformationTestAPI withTryglicerides(Double trygl) {
        this.optionalInformation.setTriglyceride(trygl);
        return this;
    }

    public MandatoryIndexInformation build(Integer age, String gender) {
        return MandatoryIndexInformation.builder()
                .age(age)
                .gender(gender)
                .fullName("-")
                .glucoseMandatory(glucoseMandatory)
                .insulinMandatory(insulinMandatory)
                .optionalInformation(optionalInformation)
                .selectedIndexes(new ArrayList<>())
                .build();
    }
}
