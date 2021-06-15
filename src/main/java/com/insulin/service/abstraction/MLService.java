package com.insulin.service.abstraction;

import com.insulin.utils.model.ClassificationModel;
import com.insulin.utils.model.ModelResult;
import com.insulin.utils.model.RegressionModel;

import java.io.IOException;

public interface MLService {
    void adaptClassificationModelValues(ClassificationModel classificationModel);

    void adaptRegressionModelValues(RegressionModel regressionModel);

    ModelResult sendToModel(ClassificationModel classificationModel) throws IOException, InterruptedException;

    ModelResult sendToModel(RegressionModel regressionModel) throws IOException, InterruptedException;
}
