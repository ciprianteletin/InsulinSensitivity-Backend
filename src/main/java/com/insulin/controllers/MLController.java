package com.insulin.controllers;

import com.insulin.service.abstraction.MLService;
import com.insulin.utils.model.ClassificationModel;
import com.insulin.utils.model.ModelResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/classification")
public class MLController {
    private final MLService mlService;

    @Autowired
    public MLController(MLService mlService) {
        this.mlService = mlService;
    }

    @PostMapping("/predict")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN')")
    public ResponseEntity<ModelResult> predictDiabetes(@Valid @RequestBody ClassificationModel dataModel) throws IOException, InterruptedException {
        mlService.adaptClassificationModelValues(dataModel);
        return new ResponseEntity<>(mlService.sendToModel(dataModel), HttpStatus.OK);
    }
}
