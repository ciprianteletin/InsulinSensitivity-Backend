package com.insulin.controllers;

import com.insulin.exceptions.model.InvalidHistoryId;
import com.insulin.exceptions.model.UserNotFoundException;
import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.service.abstraction.HistoryService;
import com.insulin.utils.model.IndexSummary;
import com.insulin.utils.model.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<List<IndexSummary>> getHistoryByUsername(@PathVariable("username") String username)
            throws UserNotFoundException {
        return new ResponseEntity<>(this.historyService.findSummaryByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/result/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<Pair<MandatoryInsulinInformation, IndexSender>> getResultsById(@PathVariable("id") Long id,
                                                                                         Authentication auth)
            throws InvalidHistoryId, UserNotFoundException {
        String principal = (String) auth.getPrincipal();
        return new ResponseEntity<>(historyService.getMandatorySenderPairByHistoryId(id, principal), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<HttpStatus> deleteByHistoryId(@PathVariable("id") Long id) {
        this.historyService.deleteByHistoryId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<HttpStatus> deleteHistoryByDate(@RequestParam(name = "fromDate") String fromDate,
                                                          @RequestParam(name = "toDate") String toDate) {
        this.historyService.deleteByCreationDateBetween(fromDate, toDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'MEDIC', 'ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllHistory() {
        this.historyService.deleteAllHistory();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
