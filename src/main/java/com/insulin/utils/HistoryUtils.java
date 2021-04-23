package com.insulin.utils;

import com.insulin.model.History;
import com.insulin.model.IndexHistory;
import com.insulin.model.form.*;
import com.insulin.utils.model.IndexSummary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.insulin.utils.MandatoryInfoBuildUtils.buildOptionalData;

public final class HistoryUtils {

    private HistoryUtils() {

    }

    public static IndexHistory buildIndexHistory(String name, IndexResult indexResult) {
        return IndexHistory.builder() //
                .indexName(name) //
                .result(indexResult.getResult()) //
                .message(indexResult.getMessage()) //
                .normalRange(indexResult.getNormalRange()) //
                .build();
    }

    public static IndexResult buildIndexResult(IndexHistory indexHistory) {
        return IndexResult.builder()
                .normalRange(indexHistory.getNormalRange()) //
                .message(indexHistory.getMessage()) //
                .result(indexHistory.getResult()) //
                .build();
    }

    private static List<IndexHistory> convertSenderToHistory(IndexSender indexSender) {
        List<IndexHistory> indexHistories = new ArrayList<>();
        indexSender.getResults().forEach((k, v) -> indexHistories.add(buildIndexHistory(k, v)));
        return indexHistories;
    }

    public static IndexSender convertHistoryToSender(History history) {
        IndexSender indexSender = new IndexSender();
        indexSender.setResult(history.getResult());
        Map<String, IndexResult> results = indexSender.getResults();
        List<IndexHistory> indexHistories = history.getIndexHistory();
        indexHistories.forEach(indexHistory -> {
            String indexName = indexHistory.getIndexName();
            results.put(indexName, buildIndexResult(indexHistory));
        });
        return indexSender;
    }

    public static IndexSummary convertHistoryToSummary(History history) {
        String[] results = history.getResult().split("\\|");
        List<IndexHistory> indexHistories = history.getIndexHistory();
        List<String> indexNames = indexHistories.stream().map(IndexHistory::getIndexName)
                .collect(Collectors.toList());
        return IndexSummary.builder() //
                .id(history.getId()) //
                .chartsResult(results[0]) //
                .indexResult(results[1]) //
                .creationDate(history.getCreationDate()) //
                .indexNames(indexNames) //
                .build();
    }

    public static MandatoryInsulinInformation convertHistoryToMandatory(History history) {
        OptionalInsulinInformation optionalInformation = new OptionalInsulinInformation();
        Set<OptionalData> optionalData = history.getOptionalData();
        populateOptionalInformationObject(optionalInformation, optionalData);
        return MandatoryInsulinInformation.builder() //
                .glucoseMandatory(history.getGlucoseMandatory()) //
                .insulinMandatory(history.getInsulinMandatory()) //
                .selectedIndexes(getIndexNamesFromIndexHistory(history.getIndexHistory())) //
                .optionalInformation(optionalInformation) //
                .build();
    }

    public static History buildHistory(MandatoryInsulinInformation mandatoryInformation, String result, IndexSender sender) {
        return History.builder() //
                .result(result) //
                .indexHistory(convertSenderToHistory(sender)) //
                .glucoseMandatory(mandatoryInformation.getGlucoseMandatory()) //
                .insulinMandatory(mandatoryInformation.getInsulinMandatory()) //
                .optionalData(buildOptionalData(mandatoryInformation)) //
                .creationDate(LocalDate.now()) //
                .build();
    }

    private static List<String> getIndexNamesFromIndexHistory(List<IndexHistory> indexHistories) {
        List<String> indexNames = new ArrayList<>();
        indexHistories.forEach(indexHistory -> indexNames.add(indexHistory.getIndexName()));
        return indexNames;
    }

    private static void populateOptionalInformationObject(OptionalInsulinInformation optionalInformation, Set<OptionalData> optionalData) {
        optionalData.forEach(opt -> matchOptionalAttribute(optionalInformation, opt));
    }

    private static void matchOptionalAttribute(OptionalInsulinInformation optionalInformation, OptionalData optionalData) {
        Double value = optionalData.getValue();
        switch (optionalData.getName()) {
            case "Height":
                optionalInformation.setHeight(value);
                break;
            case "Weight":
                optionalInformation.setWeight(value);
                break;
            case "Nefa":
                optionalInformation.setNefa(value);
                break;
            case "HDL":
                optionalInformation.setHdl(value);
                break;
            case "Thyroglobulin":
                optionalInformation.setThyroglobulin(value);
                break;
            default:
                optionalInformation.setTriglyceride(value);
                break;
        }
    }
}
