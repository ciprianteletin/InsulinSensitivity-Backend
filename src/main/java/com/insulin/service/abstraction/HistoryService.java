package com.insulin.service.abstraction;

import com.insulin.exceptions.model.InvalidHistoryId;
import com.insulin.exceptions.model.UserNotFoundException;
import com.insulin.model.User;
import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryIndexInformation;
import com.insulin.utils.model.IndexSummary;
import com.insulin.utils.model.Pair;

import java.util.List;

public interface HistoryService {
    void saveHistory(User user, MandatoryIndexInformation mandatoryInformation,
                     String result, IndexSender indexSender);

    List<IndexSummary> findSummaryByUsername(String username) throws UserNotFoundException;

    Pair<MandatoryIndexInformation, IndexSender> getMandatorySenderPairByHistoryId(Long id, String username) throws InvalidHistoryId, UserNotFoundException;

    void deleteByHistoryId(Long historyId);

    void deleteByCreationDateBetween(String from, String to);

    void deleteAllHistory();

    String getCreationDate(Long id) throws InvalidHistoryId;
}
