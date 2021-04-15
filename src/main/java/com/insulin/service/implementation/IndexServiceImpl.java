package com.insulin.service.implementation;

import com.insulin.model.form.Formula;
import com.insulin.model.form.MandatoryInsulinInformation;
import com.insulin.service.abstraction.IndexService;
import org.springframework.stereotype.Service;

@Service
public class IndexServiceImpl implements IndexService {
    private final Formula formula;

    public IndexServiceImpl() {
        this.formula = Formula.getInstance();
    }

    @Override
    public void calculateFormulas(MandatoryInsulinInformation mandatoryInformation) {

    }
}
