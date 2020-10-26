package com.koltunm.skanner.sentiment;

import org.ejml.simple.SimpleMatrix;

public class SentenceScore extends Score
{
    public SentenceScore(SimpleMatrix matrix)
    {
        setMatrix(matrix);
    }

    public ScoreClassification getClassification()
    {
        int veryNegative = (int) Math.round(getMatrix().get(0) * 100.0);
        int negative = (int) Math.round(getMatrix().get(1) * 100.0);
        int neutral = (int) Math.round(getMatrix().get(2) * 100.0);
        int positive = (int) Math.round(getMatrix().get(3) * 100.0);
        int veryPositive = (int) Math.round(getMatrix().get(4) * 100.0);

        return new ScoreClassification(veryNegative, negative, neutral, positive, veryPositive);
    }

    public String toString()
    {
        return new StringBuilder()
                .append(String.format("%-15s : %s\n", "TYPE", getType()))
                .append(String.format("%-15s : %s\n", "MATRIX", getMatrix()))
                .append(String.format("%-15s\n : %s\n", "CLASSIFICATION", getClassification()))
                .toString();
    }
}
