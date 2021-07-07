package com.insulin.controllers;

import com.insulin.excel.ExcelManager;
import com.insulin.exceptions.model.InvalidHistoryId;
import com.insulin.exceptions.model.UserNotFoundException;
import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.service.abstraction.ExcelService;
import com.insulin.service.abstraction.HistoryService;
import com.insulin.utils.model.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;
    private final HistoryService historyService;

    public ExcelController(ExcelService excelService, HistoryService historyService) {
        this.excelService = excelService;
        this.historyService = historyService;
    }

    @PostMapping("/result")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN')")
    public ResponseEntity<byte[]> getExcelResults(@RequestBody @Valid Pair<MandatoryIndexInformation, IndexSender> excelData) throws IOException {
        byte[] excelContent = this.excelService.exportResponseExcel(excelData.getFirst(), excelData.getSecond());
        return ResponseEntity.status(HttpStatus.OK).header("Filename", "insulin.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }

    @PostMapping("/history")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN')")
    public ResponseEntity<byte[]> getExcelHistory(@RequestBody List<Long> historyId, Authentication auth)
            throws InvalidHistoryId, UserNotFoundException, IOException {
        String principal = (String) auth.getPrincipal();
        ExcelManager excelManager = new ExcelManager("Insulin.xlsx");
        for (Long id : historyId) {
            String creationDate = historyService.getCreationDate(id);
            Pair<MandatoryIndexInformation, IndexSender> excelData = historyService.getMandatorySenderPairByHistoryId(id, principal);
            excelService.constructExcelDocument(excelManager, excelData.getFirst(), excelData.getSecond(), creationDate);
        }
        return ResponseEntity.status(HttpStatus.OK).header("Filename", "insulin.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelManager.getExcelDocument());
    }
}
