package com.insulin.utils;

import com.insulin.enumerations.Severity;
import com.insulin.model.form.IndexResult;
import com.insulin.model.form.IndexSender;
import org.springframework.data.util.Pair;

import java.util.*;

public final class IndexUtils {
    private IndexUtils() {

    }

    public static Pair<String, Severity> defaultPair() {
        return Pair.of("-", Severity.DEFAULT);
    }

    public static Pair<String, Severity> healthyPair() {
        return Pair.of("Healthy", Severity.HEALTHY);
    }

    public static double roundValue(double result) {
        return Math.round(result * 100.0) / 100.0;
    }

    public static IndexResult buildIndexResult(double result) {
        return IndexResult.builder() //
                .result(roundValue(result)) //
                .build();
    }

    public static IndexResult buildIndexResult(double result, Pair<String, Severity> pair) {
        return IndexResult.builder() //
                .result(roundValue(result)) //
                .message(pair.getFirst()) //
                .severity(pair.getSecond()) //
                .normalRange("-") //
                .build();
    }

    public static IndexResult buildIndexResult(double result, Pair<String, Severity> pair, String range) {
        return IndexResult.builder() //
                .result(roundValue(result)) //
                .message(pair.getFirst()) //
                .severity(pair.getSecond()) //
                .normalRange(range) //
                .build();
    }

    public static String getIndexResult(IndexSender indexSender) {
        Map<Severity, Integer> countSeverity = new HashMap<>();
        indexSender.getResults().values()
                .forEach(result -> countSeverity.compute(result.getSeverity(), (k, v) -> v == null ? 0 : ++v));
        Map.Entry<Severity, Integer> max = countSeverity.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow(); // should not be reached
        return max.getKey().getName();
    }
}
