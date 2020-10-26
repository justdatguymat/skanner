package com.koltunm.skanner.sentiment;

import org.ejml.simple.SimpleMatrix;

abstract class Score
{
    private SimpleMatrix matrix;

    public abstract ScoreClassification getClassification();

    public SentimentType getType()
    {
        return getClassification().getSentimentType();
    }

    public SimpleMatrix getMatrix()
    {
        return matrix;
    }

    public void setMatrix(SimpleMatrix matrix)
    {
        this.matrix = matrix;
    }
}
