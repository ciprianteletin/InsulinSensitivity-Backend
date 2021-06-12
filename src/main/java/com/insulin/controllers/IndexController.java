package com.insulin.controllers;

import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.service.abstraction.IndexService;
import com.insulin.service.abstraction.PdfService;
import com.insulin.utils.model.Pair;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/index")
public class IndexController {
    private final IndexService indexService;
    private final PdfService pdfService;

    @Autowired
    public IndexController(IndexService indexService, PdfService pdfService) {
        this.indexService = indexService;
        this.pdfService = pdfService;
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

    @PostMapping("/pdf")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN')")
    public ResponseEntity<byte[]> getIndexPdf(@RequestParam(name = "glucoseImg") String glucoseImg,
                                              @RequestParam(name = "insulinImg") String insulinImg,
                                              @RequestBody Pair<IndexSender, MandatoryInsulinInformation> dataPair)
            throws IOException, DocumentException {
        return ResponseEntity.status(HttpStatus.OK).header("Filename", "insulin.pdf")
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(this.pdfService.getPDF(glucoseImg, insulinImg, dataPair));
    }
}
