package com.insulin.utils.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//TODO check if needed in the future/change the impl.
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CaptchaModel {
    @JsonProperty
    private boolean activateCaptchaCode;
}
