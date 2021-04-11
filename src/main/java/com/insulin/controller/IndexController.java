package com.insulin.controller;

import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/index")
public class IndexController {
    /**
     * No login requested for this endpoint for the simple fact
     * that any user can use this feature.
     */
    @PostMapping
    public ResponseEntity<HttpStatus> evaluateIndexes(@RequestBody @Valid MandatoryInsulinInformation mandatoryInformation) {
        System.out.println(mandatoryInformation);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
