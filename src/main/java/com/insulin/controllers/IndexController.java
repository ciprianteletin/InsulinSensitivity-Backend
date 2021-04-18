package com.insulin.controllers;

import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.service.abstraction.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/index")
public class IndexController {
    private final IndexService indexService;

    @Autowired
    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * No login requested for this endpoint for the simple fact
     * that any user can use this feature.
     */
    @PostMapping("/{username}")
    public ResponseEntity<HttpStatus> evaluateIndexes(@RequestBody @Valid MandatoryInsulinInformation mandatoryInformation,
                                                      @PathVariable(name = "username", required = false) String username) {
        indexService.getIndexResult(mandatoryInformation, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
