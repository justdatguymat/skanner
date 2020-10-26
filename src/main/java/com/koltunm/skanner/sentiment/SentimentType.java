package com.koltunm.skanner.sentiment;

public enum SentimentType
{
    VERY_NEGATIVE("Very negative"),
    NEGATIVE("Negative"),
    NEUTRAL("Neutral"),
    POSITIVE("Positive"),
    VERY_POSITIVE("Very positive");

    public final String type;

    SentimentType(String type)
    {
        this.type = type;
    }
}
