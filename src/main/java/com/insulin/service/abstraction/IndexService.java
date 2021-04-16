package com.insulin.service.abstraction;

import com.insulin.model.form.MandatoryInsulinInformation;

public interface IndexService {
    void getIndexResult(MandatoryInsulinInformation mandatoryInformation, String username);
}
