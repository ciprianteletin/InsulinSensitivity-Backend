package com.insulin.service.abstraction;

import com.insulin.utils.model.ClassificationModel;
import com.insulin.utils.model.ModelResult;

import java.io.IOException;

public interface MLService {
    void adaptClassificationModelValues(ClassificationModel classificationModel);

    ModelResult sendToModel(ClassificationModel classificationModel) throws IOException, InterruptedException;
}
