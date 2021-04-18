package com.insulin.service.implementation;

import com.insulin.formula.result.Analyzer;
import com.insulin.model.User;
import com.insulin.model.form.*;
import com.insulin.repository.UserRepository;
import com.insulin.service.abstraction.HistoryService;
import com.insulin.service.abstraction.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IndexServiceImpl implements IndexService {
    private final Formula formula;
    private final Analyzer analyzer;
    private final UserRepository userRepository;
    private final HistoryService historyService;

    @Autowired
    public IndexServiceImpl(UserRepository userRepository, HistoryService historyService) {
        this.formula = Formula.getInstance();
        this.analyzer = Analyzer.getInstance();
        this.userRepository = userRepository;
        this.historyService = historyService;
    }

    @Override
    public void getIndexResult(MandatoryInsulinInformation mandatoryInformation, String username) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        String initialResult = analyzer.filterGlucoseMandatoryResult(glucoseMandatory);
        User user = getUserOrNull(username); // for preserving a history
        IndexSender sender = populateIndexSenderMap(mandatoryInformation);
        if (user != null) {
            this.historyService.saveHistory(user, mandatoryInformation, initialResult, sender);
        }
    }

    private IndexSender populateIndexSenderMap(MandatoryInsulinInformation mandatoryInformation) {
        IndexSender indexSender = new IndexSender();
        List<String> indexes = mandatoryInformation.getSelectedIndexes();
        indexes.forEach(index -> {
            IndexResult result = formula.getFormula(index).calculate(mandatoryInformation);
            indexSender.addResult(index, result);
        });
        return indexSender;
    }

    private User getUserOrNull(String username) {
        if (username == null) {
            return null;
        }
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }
}
