package com.insulin.utils.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CaptchaModel {
    @JsonProperty
    private boolean activateCaptchaCode;
}
