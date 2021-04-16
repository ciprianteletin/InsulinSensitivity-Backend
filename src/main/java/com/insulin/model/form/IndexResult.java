package com.insulin.model.form;

import com.insulin.enumerations.Severity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndexResult {
    private double result;
    private String message;
    private Severity severity;
    private String normalRange;
}
