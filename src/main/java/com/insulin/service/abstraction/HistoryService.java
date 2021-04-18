package com.insulin.service.abstraction;

import com.insulin.model.User;
import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;

public interface HistoryService {
    void saveHistory(User user, MandatoryInsulinInformation mandatoryInformation,
                     String result, IndexSender indexSender);
}
