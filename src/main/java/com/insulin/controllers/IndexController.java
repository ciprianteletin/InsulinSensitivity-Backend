package com.insulin.controllers;

import com.insulin.model.form.MandatoryInsulinInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/index")
public class IndexController {
    /**
     * No login requested for this endpoint for the simple fact
     * that any user can use this feature.
     */
    @PostMapping("/{username}")
    public ResponseEntity<HttpStatus> evaluateIndexes(@RequestBody @Valid MandatoryInsulinInformation mandatoryInformation,
                                                      @RequestParam(name = "username", required = false) String username) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
