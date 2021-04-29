package com.insulin.controllers;

import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.service.abstraction.ExcelService;
import com.insulin.utils.model.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/result")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<byte[]> getExcelResults(@RequestBody @Valid Pair<MandatoryInsulinInformation, IndexSender> excelData) throws IOException {
        byte[] excelContent = this.excelService.exportResponseExcel(excelData.getFirst(), excelData.getSecond());
        return ResponseEntity.status(HttpStatus.OK).header("Filename", "insulin.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }

}
