package com.insulin.service.abstraction;

import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryIndexInformation;

public interface IndexService {
    IndexSender getIndexResult(MandatoryIndexInformation mandatoryInformation, String username);
}
