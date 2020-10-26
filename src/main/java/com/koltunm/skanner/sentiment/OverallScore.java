package com.koltunm.skanner.sentiment;

import org.ejml.simple.SimpleMatrix;

import java.util.LinkedList;
import java.util.List;

public class OverallScore extends Score
{
    private List<SentenceScore> sentenceScoreList;

    public OverallScore()
    {
        setSentenceScoreList(new LinkedList<>());
        setMatrix(new SimpleMatrix(5, 1));
    }

    public void mergeScore(SentenceScore sentenceScore)
    {
        sentenceScoreList.add(sentenceScore);
        SimpleMatrix newMatrix = sentenceScore.getMatrix();
        newMatrix = getMatrix().plus(newMatrix);
        setMatrix(newMatrix);
    }


    @Override
    public ScoreClassification getClassification()
    {
        int veryNegative = (int) Math.round(getMatrix().get(0) * 100.0 / sentenceScoreList.size());
        int negative = (int) Math.round(getMatrix().get(1) * 100.0 / sentenceScoreList.size());
        int neutral = (int) Math.round(getMatrix().get(2) * 100.0 / sentenceScoreList.size());
        int positive = (int) Math.round(getMatrix().get(3) * 100.0 / sentenceScoreList.size());
        int veryPositive = (int) Math.round(getMatrix().get(4) * 100.0 / sentenceScoreList.size());

        return new ScoreClassification(veryNegative, negative, neutral, positive, veryPositive);
    }

    public void setSentenceScoreList(List<SentenceScore> sentenceScoreList)
    {
        this.sentenceScoreList = sentenceScoreList;
    }

    @Override
    public String toString()
    {
        return new StringBuilder()
                .append(String.format("%-15s : %s\n", "TYPE", getType()))
                .append(String.format("%-15s : %d\n", "MERGES", sentenceScoreList.size()))
                .append(String.format("%-15s : %s\n", "MATRIX", getMatrix()))
                .append(String.format("%-15s : \n%s\n", "CLASSIFICATION", getClassification()))
                .toString();
    }
}
