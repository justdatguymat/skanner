package com.koltunm.skanner.machinelearning;

import weka.classifiers.Evaluation;

public class PredictionResult
{
    final private Evaluation evaluation;
    final private double priceChange;
    private double newPrice, amountChange;

    public PredictionResult(Evaluation evaluation, double priceChange)
    {
        this.evaluation = evaluation;
        this.priceChange = priceChange;
    }

    public Evaluation getEvaluation()
    {
        return evaluation;
    }

    public double getPriceChange()
    {
        return priceChange;
    }

    public String toString()
    {
        return String.format("%s%n%nPREDICTED PRICE CHANGE : %s", evaluation.toSummaryString(true), priceChange);
    }

    public double getAmountChange()
    {
        return amountChange;
    }

    public void setAmountChange(double amountChange)
    {
        this.amountChange = amountChange;
    }

    public double getNewPrice()
    {
        return newPrice;
    }

    public void setNewPrice(double newPrice)
    {
        this.newPrice = newPrice;
    }
}
