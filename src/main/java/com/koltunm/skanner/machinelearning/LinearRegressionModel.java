package com.koltunm.skanner.machinelearning;

import com.koltunm.skanner.util.Log;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

import java.util.List;

public class LinearRegressionModel implements MachineLearning
{
    @Override
    public PredictionResult predict(DataSet dataSet) throws Exception
    {
        Instances data = dataSet.getInstances();
        data.setClassIndex(data.numAttributes() - 1);

        LinearRegression model = new LinearRegression();
        model.buildClassifier(data);
        Log.i(String.format("********* Linear Regression Model: %n %s", model));

        Evaluation evaluation = new Evaluation(data);
        evaluation.evaluateModel(model, data);
        Log.i(String.format("********* Linear Regression Evaluation: %n %s", evaluation.toSummaryString()));

        double priceChange = model.classifyInstance(data.lastInstance());
        PredictionResult result = new PredictionResult(evaluation, priceChange);
        return result;
    }

    @Override
    public Evaluation evaluate(Classifier classifier, Instances trainSet, Instances testSet) throws Exception
    {
        return null;
    }

    @Override
    public double getAccuracy(List<Prediction> predictionList)
    {
        return 0;
    }
}
