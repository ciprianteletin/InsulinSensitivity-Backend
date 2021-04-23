package com.insulin.controllers;

import com.insulin.model.form.IndexSender;
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
     * <p>
     * The returned object will represent the result displayed to the user, starting with the table of indexes
     * and also the graphical representation.
     */
    @PostMapping("/{username}")
    public ResponseEntity<IndexSender> evaluateIndexes(@RequestBody @Valid MandatoryInsulinInformation mandatoryInformation,
                                                       @PathVariable(name = "username", required = false) String username) {
        return new ResponseEntity<>(indexService.getIndexResult(mandatoryInformation, username), HttpStatus.OK);
    }
}
