package com.insulin.service.implementation;

import com.insulin.model.History;
import com.insulin.model.IndexHistory;
import com.insulin.model.User;
import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.model.form.OptionalData;
import com.insulin.model.form.OptionalInsulinInformation;
import com.insulin.repository.HistoryRepository;
import com.insulin.repository.UserRepository;
import com.insulin.service.abstraction.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository, UserRepository userRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveHistory(User user, MandatoryInsulinInformation mandatoryInformation, String result, IndexSender indexSender) {
        History history = buildHistory(mandatoryInformation, result, indexSender);
        user.addHistory(history);
        historyRepository.save(history);
        userRepository.save(user);
    }

    private History buildHistory(MandatoryInsulinInformation mandatoryInformation, String result, IndexSender sender) {
        return History.builder() //
                .result(result) //
                .indexHistory(convertSenderToHistory(sender)) //
                .glucoseMandatory(mandatoryInformation.getGlucoseMandatory()) //
                .insulinMandatory(mandatoryInformation.getInsulinMandatory()) //
                .optionalData(buildOptionalData(mandatoryInformation)) //
                .creationDate(LocalDate.now()) //
                .build();
    }

    private List<IndexHistory> convertSenderToHistory(IndexSender indexSender) {
        List<IndexHistory> indexHistories = new ArrayList<>();
        indexSender.getResults().forEach((k, v) -> indexHistories.add(IndexHistory.buildIndexHistory(k, v)));
        return indexHistories;
    }

    private Set<OptionalData> buildOptionalData(MandatoryInsulinInformation mandatoryInformation) {
        Set<OptionalData> optionalData = new HashSet<>();
        OptionalInsulinInformation optionalInfo = mandatoryInformation.getOptionalInformation();
        optionalData.add(OptionalData.buildOptionalData("Height", optionalInfo.getHeight()));
        optionalData.add(OptionalData.buildOptionalData("Weight", optionalInfo.getWeight()));
        optionalData.add(OptionalData.buildOptionalData("Nefa", optionalInfo.getNefa()));
        optionalData.add(OptionalData.buildOptionalData("HDL", optionalInfo.getHdl()));
        optionalData.add(OptionalData.buildOptionalData("Thyroglobulin", optionalInfo.getThyroglobulin()));
        optionalData.add(OptionalData.buildOptionalData("Triglyceride", optionalInfo.getTriglyceride()));
        return optionalData.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
