package com.insulin.model.form;

import com.insulin.enumerations.Severity;
import lombok.Builder;
import lombok.Data;

/**
 * Class used for storing data that will be passed to the user, storing index information.
 * It's not used as Entity because it has slight difference compared to History class
 */
@Data
@Builder
public class IndexResult {
    private double result;
    private String message;
    private Severity severity;
    private String normalRange;
}
