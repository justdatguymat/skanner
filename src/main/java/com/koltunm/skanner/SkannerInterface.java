package com.koltunm.skanner;

import com.koltunm.skanner.clients.finance.StockQuote;
import com.koltunm.skanner.clients.news.Article;
import com.koltunm.skanner.clients.twitter.Tweet;
import com.koltunm.skanner.machinelearning.DataRow;
import com.koltunm.skanner.machinelearning.PredictionResult;
import com.koltunm.skanner.sentiment.SentimentAnalyzable;
import com.koltunm.skanner.services.FetcherForm;
import com.koltunm.skanner.services.ServiceCallBack;
import com.koltunm.skanner.ui.UIAction;
import javafx.concurrent.Service;

import java.util.List;

public interface SkannerInterface
{
    Service<List<Article>> fetchArticles(ServiceCallBack callBack);
    Service<List<StockQuote>> fetchStockData(ServiceCallBack callBack);
    Service<List<Tweet>> fetchTweets(ServiceCallBack callBack);
    Service<List<SentimentAnalyzable>> analyzeSentiment(ServiceCallBack callBack);
    void setFetcherForm(FetcherForm fetcherForm);
    boolean isDataFetched();
    boolean isAnalysisCompleted();
    FetcherForm getFetcherForm();
    List<Article> getArticleList();
    List<StockQuote> getStockQuoteList();
    List<Tweet> getTweetList();
    List<SentimentAnalyzable> getSentimentAnalyzableList();
    List<DataRow> getDataRowList();
    void predict(UIAction uiAction);
    void save(String fileName);
    void load(String fileName);
    PredictionResult getPredictionResult();

    //public Task<List<>> analyzeSentiment(List<SentimentAnalyzable> list);
    /*
    public StockMetric fetchStockData(String tickerSymbol);
    public TwitchMetric fetchTwitchData(List<String> tags);
    public void trainModel();
    public void startlll
    */
}
