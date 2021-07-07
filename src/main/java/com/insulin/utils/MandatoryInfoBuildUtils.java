package com.insulin.utils;

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

    public static OptionalIndexInformation buildOptional(OptionalIndexInformation optionalInformation) {
        return OptionalIndexInformation.builder() //
                .hdl(optionalInformation.getHdl()) //
                .height(optionalInformation.getHeight()) //
                .weight(optionalInformation.getWeight()) //
                .nefa(optionalInformation.getNefa()) //
                .thyroglobulin(optionalInformation.getThyroglobulin()) //
                .triglyceride(optionalInformation.getTriglyceride()) //
                .build();
    }

    public static Set<OptionalData> buildOptionalData(MandatoryIndexInformation mandatoryInformation) {
        Set<OptionalData> optionalData = new HashSet<>();
        OptionalIndexInformation optionalInfo = mandatoryInformation.getOptionalInformation();
        optionalData.add(OptionalData.buildOptionalData("Height", optionalInfo.getHeight()));
        optionalData.add(OptionalData.buildOptionalData("Weight", optionalInfo.getWeight()));
        optionalData.add(OptionalData.buildOptionalData("Nefa", optionalInfo.getNefa()));
        optionalData.add(OptionalData.buildOptionalData("HDL", optionalInfo.getHdl()));
        optionalData.add(OptionalData.buildOptionalData("Thyroglobulin", optionalInfo.getThyroglobulin()));
        optionalData.add(OptionalData.buildOptionalData("Triglyceride", optionalInfo.getTriglyceride()));
        return optionalData.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
