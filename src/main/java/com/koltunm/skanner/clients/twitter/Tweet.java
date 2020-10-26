package com.koltunm.skanner.clients.twitter;

import com.koltunm.skanner.sentiment.SentimentAnalyzable;
import com.koltunm.skanner.ui.components.TweetFeed;
import twitter4j.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Tweet extends SentimentAnalyzable
{
    private String author, message;
    private LocalDateTime date;

    private TweetFeed tweetFeed;

    public Tweet(Status status)
    {
        this.author = status.getUser().getScreenName();
        this.message = status.getText();
        this.date = status.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    public String sentimentString()
    {
        return getMessage();
    }

    @Override
    public String getSentimentHeading()
    {
        return String.format("TWEET: %s", getMessage());
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public LocalDateTime getDateTime()
    {
        return date;
    }

    public LocalDate getDate()
    {
        return date.toLocalDate();
    }

    public void setDate(LocalDateTime date)
    {
        this.date = date;
    }

    public TweetFeed getTweetFeed()
    {
        if (tweetFeed == null)
            this.tweetFeed = new TweetFeed(date, author, message);
        return tweetFeed;
    }
}
