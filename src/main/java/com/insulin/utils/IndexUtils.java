package com.insulin.utils;

import com.insulin.enumerations.Severity;
import com.insulin.model.form.IndexResult;
import org.springframework.data.util.Pair;

public final class IndexUtils {
    private IndexUtils() {

    }

    public static Pair<String, Severity> defaultPair() {
        return Pair.of("-", Severity.DEFAULT);
    }

    public static Pair<String, Severity> healthyPair() {
        return Pair.of("Healthy", Severity.HEALTHY);
    }

    public static IndexResult buildIndexResult(double result) {
        return IndexResult.builder() //
                .result(result) //
                .build();
    }

    public static IndexResult buildIndexResult(double result, Pair<String, Severity> pair) {
        return IndexResult.builder() //
                .result(result) //
                .message(pair.getFirst()) //
                .severity(pair.getSecond()) //
                .normalRange("-") //
                .build();
    }

    public static IndexResult buildIndexResult(double result, Pair<String, Severity> pair, String range) {
        return IndexResult.builder() //
                .result(result) //
                .message(pair.getFirst()) //
                .severity(pair.getSecond()) //
                .normalRange(range) //
                .build();
    }
}
