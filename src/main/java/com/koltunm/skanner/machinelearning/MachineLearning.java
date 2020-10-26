package com.koltunm.skanner.machinelearning;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.core.Instances;

import java.util.List;

public interface MachineLearning
{
    public PredictionResult predict(DataSet dataSet) throws Exception;
    public Evaluation evaluate(Classifier classifier, Instances trainSet,Instances testSet) throws Exception;
    public double getAccuracy(List<Prediction> predictionList);
}
