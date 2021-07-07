package com.insulin.service.implementation;

import com.insulin.formula.result.Analyzer;
import com.insulin.model.User;
import com.insulin.model.form.*;
import com.insulin.repository.UserRepository;
import com.insulin.service.abstraction.HistoryService;
import com.insulin.service.abstraction.IndexService;
import com.insulin.utils.IndexUtils;
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
    public IndexSender getIndexResult(MandatoryIndexInformation mandatoryInformation, String username) {
        GlucoseMandatory glucoseMandatory = mandatoryInformation.getGlucoseMandatory();
        String initialResult = analyzer.filterGlucoseMandatoryResult(glucoseMandatory);
        Optional<User> user = getUserOrNull(username); // for preserving a history
        IndexSender sender = populateIndexSenderMap(mandatoryInformation);
        String result = generateResult(initialResult, sender);
        sender.setResult(result);
        user.ifPresent(u -> this.historyService.saveHistory(u, mandatoryInformation, result, sender));
        return sender;
    }

    private IndexSender populateIndexSenderMap(MandatoryIndexInformation mandatoryInformation) {
        IndexSender indexSender = new IndexSender();
        List<String> indexes = mandatoryInformation.getSelectedIndexes();
        indexes.forEach(index -> {
            IndexResult result = formula.getFormula(index).calculate(mandatoryInformation);
            indexSender.addResult(index, result);
        });
        return indexSender;
    }

    private Optional<User> getUserOrNull(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username);
    }

    private String generateResult(String result, IndexSender sender) {
        String computedResult = IndexUtils.getIndexResult(sender);
        return result + "|" + computedResult;
    }
}
