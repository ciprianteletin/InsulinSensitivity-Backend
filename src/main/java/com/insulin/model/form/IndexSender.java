package com.insulin.model.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IndexSender {
    private String result;
    private final Map<String, IndexResult> results = new HashMap<>();

    public void addResult(String indexName, IndexResult result) {
        results.put(indexName, result);
    }

    public IndexResult getResult(String indexName) {
        return results.get(indexName);
    }
}
