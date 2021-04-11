package com.insulin.utils.model;

import com.insulin.model.form.*;

/**
 * Static class used for building different objects related to index calculation.
 * By using a builder, we can obtain different shallow copies that can be
 * manipulated without affecting the main object.
 * <p>
 * In order to avoid to change attributes of a common reference,
 * multiple build options will be available.
 */
public final class MandatoryInfoBuildUtils {
    private MandatoryInfoBuildUtils() {

    }

    /**
     * Build deep copy of MandatoryInsulinInformation object, by building a new object for
     * every inner one, so that a conversion of measurements units does not affect the principal
     * object.
     * <p>
     * There is no need to deep copy the list of indices, as it's not modifiable.
     */
    public static MandatoryInsulinInformation buildMandatory(MandatoryInsulinInformation mandatoryInformation) {
        return MandatoryInsulinInformation.builder() //
                .username(mandatoryInformation.getUsername()) //
                .age(mandatoryInformation.getAge()) //
                .fullName(mandatoryInformation.getFullName()) //
                .gender(mandatoryInformation.getGender()) //
                .glucoseMandatory(buildGlucose(mandatoryInformation.getGlucoseMandatory())) //
                .placeholders(buildPlaceholders(mandatoryInformation.getPlaceholders())) //
                .insulinMandatory(buildInsulin(mandatoryInformation.getInsulinMandatory())) //
                .selectedIndexes(mandatoryInformation.getSelectedIndexes()) //
                .optionalInformation(buildOptional(mandatoryInformation.getOptionalInformation())) //
                .build();
    }

    public static GlucoseMandatory buildGlucose(GlucoseMandatory glucoseMandatory) {
        return GlucoseMandatory.builder() //
                .fastingGlucose(glucoseMandatory.getFastingGlucose()) //
                .glucoseThree(glucoseMandatory.getGlucoseThree()) //
                .glucoseSix(glucoseMandatory.getGlucoseSix()) //
                .glucoseOneTwenty(glucoseMandatory.getGlucoseOneTwenty()) //
                .build();
    }

    public static InsulinMandatory buildInsulin(InsulinMandatory insulinMandatory) {
        return InsulinMandatory.builder() //
                .fastingInsulin(insulinMandatory.getFastingInsulin()) //
                .insulinThree(insulinMandatory.getInsulinThree()) //
                .insulinSix(insulinMandatory.getInsulinSix()) //
                .insulinOneTwenty(insulinMandatory.getInsulinOneTwenty()) //
                .build();
    }

    public static Placeholders buildPlaceholders(Placeholders placeholders) {
        return Placeholders.builder() //
                .glucosePlaceholder(placeholders.getGlucosePlaceholder()) //
                .insulinPlaceholder(placeholders.getInsulinPlaceholder()) //
                .build();
    }

    public static OptionalInsulinInformation buildOptional(OptionalInsulinInformation optionalInformation) {
        return OptionalInsulinInformation.builder() //
                .hdl(optionalInformation.getHdl()) //
                .height(optionalInformation.getHeight()) //
                .weight(optionalInformation.getWeight()) //
                .nefa(optionalInformation.getNefa()) //
                .thyroglobulin(optionalInformation.getThyroglobulin()) //
                .triglyceride(optionalInformation.getTriglyceride()) //
                .build();
    }
}
