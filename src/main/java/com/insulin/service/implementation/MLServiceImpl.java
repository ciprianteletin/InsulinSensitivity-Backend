package com.insulin.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.insulin.service.abstraction.MLService;
import com.insulin.utils.model.ClassificationModel;
import com.insulin.utils.model.ModelResult;
import com.insulin.utils.model.RegressionModel;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.insulin.formula.ValueConverter.*;
import static com.insulin.shared.constants.SecurityConstants.FLASK_API;
import static com.insulin.utils.ApiCommunicationUtils.getStringResponse;
import static com.insulin.utils.ApiCommunicationUtils.obtainPostClassificationRequest;

/**
 * Service which prepares the data to be used inside the models and also is responsible
 * with calling and extracting information from the second API.
 */
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
    public void adaptRegressionModelValues(RegressionModel regressionModel) {
        convertMgMmol(regressionModel);

        setCholHDLRatio(regressionModel);

        setLogTriglycerides(regressionModel);
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

    @Override
    public ModelResult sendToModel(RegressionModel regressionModel) throws IOException, InterruptedException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = writer.writeValueAsString(regressionModel);
        HttpRequest request = obtainPostClassificationRequest(FLASK_API + "/classification/progress", json);

        HttpResponse<String> response = getStringResponse(request);

        Double evolution = Double.parseDouble(response.body());
        evolution = roundValue(evolution);

        ModelResult modelResult = new ModelResult();
        modelResult.setResult(evolution);

        setDiagnosisCategory(modelResult);
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
        String placeholder = classificationModel.getPlaceholder();
        if (placeholder.equals("mmol/L")) {
            classificationModel.setGlucose(roundValue(convertSingleGlucose(classificationModel.getGlucose(), placeholder, "mg/dL")));
            classificationModel.setCholesterol(roundValue(convertCholesterolMgdl(classificationModel.getCholesterol(), placeholder)));
            classificationModel.setHdl(roundValue(convertCholesterolMgdl(classificationModel.getHdl(), placeholder)));
        }
    }

    private void setLogTriglycerides(RegressionModel regressionModel) {
        regressionModel.setLtg(Math.log10(regressionModel.getLtg()));
    }

    private void convertMgMmol(RegressionModel regressionModel) {
        String placeholder = regressionModel.getPlaceholder();
        if (placeholder.equals("mmol/L")) {
            regressionModel.setGlucose(roundValue(convertSingleGlucose(regressionModel.getGlucose(), placeholder, "mg/dL")));
            regressionModel.setCholesterol(roundValue(convertCholesterolMgdl(regressionModel.getCholesterol(), placeholder)));
            regressionModel.setHdl(roundValue(convertCholesterolMgdl(regressionModel.getHdl(), placeholder)));
            regressionModel.setLdl(roundValue(convertCholesterolMgdl(regressionModel.getLdl(), placeholder)));
            regressionModel.setLtg(roundValue(convertTriglycerideMgdl(regressionModel.getLtg(), placeholder)));
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

    private void setCholHDLRatio(RegressionModel regressionModel) {
        Double chol = regressionModel.getCholesterol();
        Double hdl = regressionModel.getHdl();

        regressionModel.setTch(roundValue(chol / hdl).intValue());
    }

    private Double roundValue(Double value) {
        return Math.round(value * 1000d) / 1000d;
    }

    private void setDiagnosisCategory(ModelResult modelResult) {
        Double result = modelResult.getResult();
        int category = 2;
        if (result < 150) {
            category = 0;
        } else if (result < 250) {
            category = 1;
        }
        modelResult.setCategory(category);
    }
}
