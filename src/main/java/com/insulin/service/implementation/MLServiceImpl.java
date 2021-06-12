package com.insulin.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.insulin.service.abstraction.MLService;
import com.insulin.utils.model.ClassificationModel;
import com.insulin.utils.model.ModelResult;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.shared.constants.SecurityConstants.FLASK_API;
import static com.insulin.utils.ApiCommunicationUtils.getStringResponse;
import static com.insulin.utils.ApiCommunicationUtils.obtainPostClassificationRequest;

@Service
public class MLServiceImpl implements MLService {
    @Override
    public void adaptClassificationModelValues(ClassificationModel classificationModel) {
        convertMgMmol(classificationModel);

        setCholHDLRatio(classificationModel);

        setBmi(classificationModel);

        classificationModel.setWeight(roundValue(classificationModel.getWeight() * 2.204)); // to lbs

        convertToInch(classificationModel);

        setWaistHipRatio(classificationModel);
    }

    @Override
    public ModelResult sendToModel(ClassificationModel classificationModel) throws IOException, InterruptedException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = writer.writeValueAsString(classificationModel);
        HttpRequest request = obtainPostClassificationRequest(FLASK_API + "/classification/diabetes", json);

        HttpResponse<String> response = getStringResponse(request);

        JSONObject jsonObject = new JSONObject(response.body());
        ModelResult modelResult = new ModelResult();
        Double probability = Math.round(jsonObject.getDouble("Probability") * 100d) / 100d;
        modelResult.setResult(jsonObject.getDouble("Result"));
        modelResult.setProbability(probability);

        return modelResult;
    }

    private void setBmi(ClassificationModel classificationModel) {
        Double height = classificationModel.getHeight() / 100;

        double bmi = (classificationModel.getWeight()) / (height * height);

        classificationModel.setBmi(bmi);
    }

    private void convertToInch(ClassificationModel classificationModel) {
        classificationModel.setHeight(roundValue(classificationModel.getHeight() * 0.393)); // inch
        classificationModel.setWaist(roundValue(classificationModel.getWaist() * 0.393));
        classificationModel.setHip(roundValue(classificationModel.getHip() * 0.393));
    }

    private void convertMgMmol(ClassificationModel classificationModel) {
        if (classificationModel.getPlaceholder().equals("mmol/L")) {
            classificationModel.setGlucose(roundValue(convertSingleGlucose(classificationModel.getGlucose(), "mmol/L", "mg/dL")));
            classificationModel.setCholesterol(roundValue(convertSingleGlucose(classificationModel.getCholesterol(), "mmol/L", "mg/dL")));
            classificationModel.setHdl(roundValue(convertSingleGlucose(classificationModel.getHdl(), "mmol/L", "mg/dL")));
        }
    }

    private void setWaistHipRatio(ClassificationModel classificationModel) {
        Double hip = classificationModel.getHip();
        Double waist = classificationModel.getWaist();
        classificationModel.setWaistHipRatio(roundValue(waist / hip));
    }

    private void setCholHDLRatio(ClassificationModel classificationModel) {
        Double chol = classificationModel.getCholesterol();
        Double hdl = classificationModel.getHdl();
        classificationModel.setCholHDLRatio(roundValue(chol / hdl));
    }

    private Double roundValue(Double value) {
        return Math.round(value * 1000d) / 1000d;
    }
}
