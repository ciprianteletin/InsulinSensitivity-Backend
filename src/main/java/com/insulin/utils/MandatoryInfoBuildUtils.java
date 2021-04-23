package com.insulin.utils;

import com.insulin.model.IndexHistory;
import com.insulin.model.form.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
                .age(mandatoryInformation.getAge()) //
                .fullName(mandatoryInformation.getFullName()) //
                .gender(mandatoryInformation.getGender()) //
                .glucoseMandatory(buildGlucose(mandatoryInformation.getGlucoseMandatory())) //
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
                .glucosePlaceholder(glucoseMandatory.getGlucosePlaceholder()) //
                .build();
    }

    public static InsulinMandatory buildInsulin(InsulinMandatory insulinMandatory) {
        return InsulinMandatory.builder() //
                .fastingInsulin(insulinMandatory.getFastingInsulin()) //
                .insulinThree(insulinMandatory.getInsulinThree()) //
                .insulinSix(insulinMandatory.getInsulinSix()) //
                .insulinOneTwenty(insulinMandatory.getInsulinOneTwenty()) //
                .insulinPlaceholder(insulinMandatory.getInsulinPlaceholder()) //
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

    public static Set<OptionalData> buildOptionalData(MandatoryInsulinInformation mandatoryInformation) {
        Set<OptionalData> optionalData = new HashSet<>();
        OptionalInsulinInformation optionalInfo = mandatoryInformation.getOptionalInformation();
        optionalData.add(OptionalData.buildOptionalData("Height", optionalInfo.getHeight()));
        optionalData.add(OptionalData.buildOptionalData("Weight", optionalInfo.getWeight()));
        optionalData.add(OptionalData.buildOptionalData("Nefa", optionalInfo.getNefa()));
        optionalData.add(OptionalData.buildOptionalData("HDL", optionalInfo.getHdl()));
        optionalData.add(OptionalData.buildOptionalData("Thyroglobulin", optionalInfo.getThyroglobulin()));
        optionalData.add(OptionalData.buildOptionalData("Triglyceride", optionalInfo.getTriglyceride()));
        return optionalData.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
