package com.insulin.service.abstraction;

import com.insulin.model.form.IndexSender;
import com.insulin.model.form.MandatoryInsulinInformation;

public interface IndexService {
    IndexSender getIndexResult(MandatoryInsulinInformation mandatoryInformation, String username);
}
