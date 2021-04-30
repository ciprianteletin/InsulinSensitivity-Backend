package com.insulin.service.abstraction;

import com.insulin.exceptions.model.InvalidHistoryId;
import com.insulin.exceptions.model.UserNotFoundException;
import com.insulin.model.User;
import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.utils.model.IndexSummary;
import com.insulin.utils.model.Pair;

import java.util.List;

public interface HistoryService {
    void saveHistory(User user, MandatoryInsulinInformation mandatoryInformation,
                     String result, IndexSender indexSender);

    List<IndexSummary> findSummaryByUsername(String username) throws UserNotFoundException;

    Pair<MandatoryInsulinInformation, IndexSender> getMandatorySenderPairByHistoryId(Long id, String username) throws InvalidHistoryId, UserNotFoundException;

    void deleteByHistoryId(Long historyId);

    String getCreationDate(Long id) throws InvalidHistoryId;
}
