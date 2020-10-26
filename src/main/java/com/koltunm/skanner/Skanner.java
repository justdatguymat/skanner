package com.koltunm.skanner;

import com.koltunm.skanner.clients.finance.FinanceInterval;
import com.koltunm.skanner.clients.finance.StockQuote;
import com.koltunm.skanner.clients.news.Article;
import com.koltunm.skanner.clients.twitter.Tweet;
import com.koltunm.skanner.machinelearning.*;
import com.koltunm.skanner.sentiment.SentimentAnalyzable;
import com.koltunm.skanner.services.*;
import com.koltunm.skanner.ui.UIAction;
import com.koltunm.skanner.util.Log;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class Skanner implements SkannerInterface
{
    private static Skanner instance = null;
    private List<Article> articleList;
    private List<StockQuote> stockQuoteList;
    private List<Tweet> tweetList;
    private List<DataRow> dataRowList;
    private FetcherForm fetcherForm;
    private DataSet dataSet;
    private PredictionResult predictionResult;

    private boolean analysisCompleted;

    public static Skanner getInstance()
    {
        return init();
    }

    public Skanner()
    {
        analysisCompleted = false;
    }

    static Skanner init()
    {
        if (Skanner.instance == null)
        {
            Skanner.instance = new Skanner();
        }
        return Skanner.instance;
    }

    public Service<List<Article>> fetchArticles(ServiceCallBack callBack)
    {
        Log.i("Creating a new Article Fetcher");
        Service<List<Article>> service = new Service<>()
        {
            @Override
            protected Task<List<Article>> createTask()
            {
                return new ArticleFetcher(fetcherForm);
            }
        };
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                Log.i("Finished fetching articles.");
                setArticleList(service.getValue());
                callBack.worksDone();
            }
        });
        callBack.registerService();
        return service;
    }

    public Service<List<StockQuote>> fetchStockData(ServiceCallBack callBack)
    {
        Log.i("Creating a new Stock Data Fetcher.");
        Service<List<StockQuote>> service = new Service<>()
        {
            @Override
            protected Task<List<StockQuote>> createTask()
            {
                return new StockDataFetcher(fetcherForm, FinanceInterval.ONE_DAY);
            }
        };
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                Log.i("Finished fetching stock date.");
                setStockQuoteList(service.getValue());
                callBack.worksDone();
            }
        });
        callBack.registerService();
        return service;
    }

    public Service<List<Tweet>> fetchTweets(ServiceCallBack callBack)
    {
        Log.i("Creating a new Twitter Fetcher.");
        Service<List<Tweet>> service = new Service<>()
        {
            @Override
            protected Task<List<Tweet>> createTask()
            {
                return new TwitterFetcher(fetcherForm);
            }
        };
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                Log.i("Finished fetching tweets.");
                setTweetList(service.getValue());
                callBack.worksDone();
            }
        });
        callBack.registerService();
        return service;
    }

    public Service<List<SentimentAnalyzable>> analyzeSentiment(ServiceCallBack callBack)
    {
        Log.i("Creating a new Twitter Fetcher.");
        Service<List<SentimentAnalyzable>> service = new Service<>()
        {
            @Override
            protected Task<List<SentimentAnalyzable>> createTask()
            {
                return new SentimentAnalyser(getSentimentAnalyzableList());
            }
        };
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                Log.i("Analysis completed!");
                analysisCompleted = true;
                callBack.worksDone();
            }
        });
        callBack.registerService();
        return service;
    }

    public void genDataRow()
    {
        CombinedData data = new CombinedData(
                getTweetList(),
                getArticleList(),
                getStockQuoteList(),
                getSentimentAnalyzableList()
        );
        DataRowBuilder dataRowBuilder = new DataRowBuilder(data, fetcherForm.getGroupingRange());
        dataRowList = dataRowBuilder.getRowList();
    }

    public boolean isAnalysisCompleted()
    {
        return analysisCompleted;
    }

    public boolean isDataFetched()
    {
        if (getArticleList() == null || getTweetList() == null || getStockQuoteList() == null)
        {
            return false;
        }
        else
        {
            return !getArticleList().isEmpty() && !getStockQuoteList().isEmpty() && !getTweetList().isEmpty();
        }
    }

    public List<SentimentAnalyzable> getSentimentAnalyzableList()
    {
        List<SentimentAnalyzable> list = new ArrayList<>(getArticleList());
        list.addAll(getTweetList());
        return list;
    }

    public List<DataRow> getDataRowList()
    {
        genDataRow();
        return dataRowList;
    }

    @Override
    public void predict(UIAction uiAction)
    {
        MachineLearning model = new LinearRegressionModel();
        try
        {
            predictionResult = model.predict(getDataSet());
            setPredictionResult(predictionResult);
            Log.i(String.format("Predicted price change is %n%s", predictionResult));

            double currentPrice = getDataRowList().get(getDataRowList().size() - 1).getClosePrice();
            double amountChange = currentPrice * (predictionResult.getPriceChange() / 100);
            double newPrice = currentPrice + amountChange;
            predictionResult.setAmountChange(amountChange);
            predictionResult.setNewPrice(newPrice);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Failed to predict: ", e);
        }
        uiAction.onComplete();
    }

    public void load(String fileName)
    {
        dataSet = new DataSet(fileName);
        Log.i(dataSet);
    }

    public void save(String fileName)
    {
        genDataRow();
        getDataSet().writeToFile(fileName);
    }

    public List<Article> getArticleList()
    {
        return articleList;
    }

    public List<StockQuote> getStockQuoteList()
    {
        return stockQuoteList;
    }

    public List<Tweet> getTweetList()
    {
        return tweetList;
    }

    public void setArticleList(List<Article> articleList)
    {
        this.articleList = articleList;
    }

    public void setStockQuoteList(List<StockQuote> stockQuoteList)
    {
        this.stockQuoteList = stockQuoteList;
    }

    public void setTweetList(List<Tweet> tweetList)
    {
        this.tweetList = tweetList;
    }

    public FetcherForm getFetcherForm()
    {
        return fetcherForm;
    }

    public void setFetcherForm(FetcherForm fetcherForm)
    {
        this.fetcherForm = fetcherForm;
    }

    public DataSet getDataSet()
    {
        return new DataSet(getDataRowList());
    }

    public PredictionResult getPredictionResult()
    {
        return predictionResult;
    }

    public void setPredictionResult(PredictionResult predictionResult)
    {
        this.predictionResult = predictionResult;
    }
}
