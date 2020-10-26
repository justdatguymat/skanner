package com.koltunm.skanner.sentiment;

import java.util.Comparator;
import java.util.TreeMap;

public class ScoreClassification
{
    private int veryNegative, negative, neutral, positive, veryPositive;
    private TreeMap<Integer, SentimentType> scoreMap;

    public ScoreClassification()
    {
        set(0,0,0,0,0);
    }

    public ScoreClassification(int veryNegative, int negative, int neutral, int positive, int veryPositive)
    {
        set(veryNegative, negative, neutral, positive, veryPositive);
    }

    private void set(int veryNegative, int negative, int neutral, int positive, int veryPositive)
    {
        this.veryNegative = veryNegative;
        this.negative = negative;
        this.neutral = neutral;
        this.positive = positive;
        this.veryPositive = veryPositive;

        this.scoreMap = new TreeMap<>(new Comparator<Integer>()
        {
            @Override
            public int compare(Integer t1, Integer t2)
            {
                return t2.compareTo(t1);
            }
        });

        this.scoreMap.put(veryNegative, SentimentType.VERY_NEGATIVE);
        this.scoreMap.put(negative, SentimentType.NEGATIVE);
        this.scoreMap.put(neutral, SentimentType.NEUTRAL);
        this.scoreMap.put(positive, SentimentType.POSITIVE);
        this.scoreMap.put(veryPositive, SentimentType.VERY_POSITIVE);
    }

    public SentimentType getSentimentType()
    {
        return scoreMap.firstEntry().getValue();
    }

    public String toString()
    {
        return new StringBuilder()
                .append(String.format("\t%-20s: %d%%\n", "Very negative", veryNegative))
                .append(String.format("\t%-20s: %d%%\n", "Negative", negative))
                .append(String.format("\t%-20s: %d%%\n", "Neutral", neutral))
                .append(String.format("\t%-20s: %d%%\n", "Positive", positive))
                .append(String.format("\t%-20s: %d%%\n", "Very positive", veryPositive))
                .toString();
    }

    public int getVeryNegative()
    {
        return veryNegative;
    }

    public void setVeryNegative(int veryNegative)
    {
        this.veryNegative = veryNegative;
    }

    public int getNegative()
    {
        return negative;
    }

    public void setNegative(int negative)
    {
        this.negative = negative;
    }

    public int getNeutral()
    {
        return neutral;
    }

    public void setNeutral(int neutral)
    {
        this.neutral = neutral;
    }

    public int getPositive()
    {
        return positive;
    }

    public void setPositive(int positive)
    {
        this.positive = positive;
    }

    public int getVeryPositive()
    {
        return veryPositive;
    }

    public void setVeryPositive(int veryPositive)
    {
        this.veryPositive = veryPositive;
    }
}
