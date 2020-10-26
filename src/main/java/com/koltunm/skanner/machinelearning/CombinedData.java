package com.koltunm.skanner.machinelearning;

import com.koltunm.skanner.clients.finance.StockQuote;
import com.koltunm.skanner.clients.news.Article;
import com.koltunm.skanner.clients.twitter.Tweet;
import com.koltunm.skanner.sentiment.SentimentAnalyzable;

import java.util.List;

public class CombinedData
{
    final private List<Tweet> tweetList;
    final private List<Article> articleList;
    final private List<StockQuote> stockQuoteList;
    final private List<SentimentAnalyzable> sentimentAnalyzableList;

    public CombinedData(List<Tweet> tweetList,
                        List<Article> articleList,
                        List<StockQuote> stockQuoteList,
                        List<SentimentAnalyzable> sentimentAnalyzableList)
    {
        this.tweetList = tweetList;
        this.articleList = articleList;
        this.stockQuoteList = stockQuoteList;
        this.sentimentAnalyzableList = sentimentAnalyzableList;
    }

    public List<Tweet> getTweetList()
    {
        return tweetList;
    }

    public List<Article> getArticleList()
    {
        return articleList;
    }

    public List<StockQuote> getStockQuoteList()
    {
        return stockQuoteList;
    }

    public List<SentimentAnalyzable> getSentimentAnalyzableList()
    {
        return sentimentAnalyzableList;
    }
}
