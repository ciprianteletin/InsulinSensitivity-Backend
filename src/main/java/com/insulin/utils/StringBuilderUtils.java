package com.insulin.utils;

import static java.util.Objects.nonNull;

public class StringBuilderUtils {
    private StringBuilder stringBuilder;

    public StringBuilderUtils() {
        stringBuilder = new StringBuilder();
    }

    public StringBuilderUtils appendIfNotNull(String text, String splitter) {
        if (nonNull(text)) {
            stringBuilder.append(text).append(splitter);
        }
        return this;
    }

    public String getResult() {
        return stringBuilder.toString();
    }
}
