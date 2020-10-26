package com.koltunm.skanner.machinelearning;

import com.koltunm.skanner.clients.finance.StockQuote;
import com.koltunm.skanner.clients.news.Article;
import com.koltunm.skanner.clients.twitter.Tweet;
import com.koltunm.skanner.sentiment.SentimentAnalyzable;
import com.koltunm.skanner.sentiment.SentimentType;
import com.koltunm.skanner.util.Log;

import java.time.LocalDate;

public class DataRow
{
    private LocalDate date;
    private int positives;
    private int negatives;
    private int neutrals;
    private int totalTweets;
    private int totalArticles;
    private StockQuote stockQuote;
    private double changePrice;

    public DataRow(StockQuote stockQuote)
    {
        this.date = stockQuote.getDate();
        this.stockQuote = stockQuote;
        this.positives = 0;
        this.negatives = 0;
        this.neutrals = 0;
        this.totalTweets = 0;
        this.totalArticles = 0;
        this.changePrice = 0d;
    }

    public DataRow(LocalDate date,
                   StockQuote stockQuote,
                   int positives,
                   int negatives,
                   int neutrals,
                   int total_tweets,
                   int total_articles)
    {
        this.date = date;
        this.positives = positives;
        this.negatives = negatives;
        this.neutrals = neutrals;
        this.totalTweets = total_tweets;
        this.totalArticles = total_articles;
        this.stockQuote = stockQuote;
        this.changePrice = 0d;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public int getPositives()
    {
        return positives;
    }

    public int getNegatives()
    {
        return negatives;
    }

    public int getNeutrals()
    {
        return neutrals;
    }

    public int getTotalTweets()
    {
        return totalTweets;
    }

    public int getTotalArticles()
    {
        return totalArticles;
    }

    public double getClosePrice()
    {
        return stockQuote.getClose().doubleValue();
    }

    public double getOpenPrice()
    {
        return stockQuote.getOpen().doubleValue();
    }

    public double getHighPrice()
    {
        return stockQuote.getHigh().doubleValue();
    }

    public double getLowPrice()
    {
        return stockQuote.getLow().doubleValue();
    }

    public long getVolume()
    {
        return stockQuote.getVolume();
    }

    public void processItem(SentimentAnalyzable item)
    {
        Log.i(String.format("PROCESSING Instance: %s \ttype: %s%n", item.getClass().getName(), item.getSentimentType()));
        incrementSentimentCount(item.getSentimentType());
        incrementTotalCount(item);
    }

    private void incrementSentimentCount(SentimentType type)
    {
        if (type == SentimentType.POSITIVE || type == SentimentType.VERY_POSITIVE)
        {
            this.positives++;
        }
        else if (type == SentimentType.NEGATIVE || type == SentimentType.VERY_NEGATIVE)
        {
            this.negatives++;
        }
        else
        {
            this.neutrals++;
        }
    }

    private void incrementTotalCount(SentimentAnalyzable item)
    {
        if (item instanceof Article)
        {
            this.totalArticles++;
        }
        else if (item instanceof Tweet)
        {
            this.totalTweets++;
        }
        else
        {
            Log.w("NOT FUCKING WORKING!!!");
        }
    }

    public double getChangePrice()
    {
        return this.changePrice;
    }

    public void setChangePriceFuturePrice(double changePrice)
    {
        double p = ((changePrice - getClosePrice()) / getClosePrice()) * 100d;
        this.changePrice = p;
    }
}
