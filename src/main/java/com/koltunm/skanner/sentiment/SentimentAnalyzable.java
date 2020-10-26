package com.koltunm.skanner.sentiment;

import java.time.LocalDateTime;

public abstract class SentimentAnalyzable
{
    private OverallScore sentimentScore;
    public abstract String sentimentString();
    public abstract String getSentimentHeading();
    public abstract LocalDateTime getDateTime();
    private int timesAnalyzed = 0;

    public OverallScore getSentimentScore(Analyser analyser)
    {
        inc();
        if(sentimentScore == null)
        {
            sentimentScore = analyser.getSentimentScore(sentimentString());
        }
        return sentimentScore;
    }

    public SentimentType getSentimentType()
    {
        return sentimentScore.getClassification().getSentimentType();
    }
    public int getVeryNegative()
    {
        return sentimentScore.getClassification().getVeryNegative();
    }
    public int getNegative()
    {
        return sentimentScore.getClassification().getNegative();
    }
    public int getNeutral()
    {
        return sentimentScore.getClassification().getNeutral();
    }
    public int getPositive()
    {
        return sentimentScore.getClassification().getPositive();
    }
    public int getVeryPositive()
    {
        return sentimentScore.getClassification().getVeryPositive();
    }
    public synchronized void inc()
    {
        timesAnalyzed++;
    }
    public int getCount()
    {
        return timesAnalyzed;
    }
}
