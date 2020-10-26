package com.koltunm.skanner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.koltunm.skanner.clients.finance.FinanceInterval;
import com.koltunm.skanner.clients.finance.StockQuote;
import com.koltunm.skanner.clients.finance.YahooClient;
import com.koltunm.skanner.clients.news.Article;
import com.koltunm.skanner.clients.news.NewsClient;
import com.koltunm.skanner.clients.twitter.Tweet;
import com.koltunm.skanner.machinelearning.*;
import com.koltunm.skanner.sentiment.Analyser;
import com.koltunm.skanner.sentiment.OverallScore;
import com.koltunm.skanner.sentiment.SentimentType;
import com.koltunm.skanner.services.*;
import com.koltunm.skanner.ui.UIAction;
import com.koltunm.skanner.util.Config;
import com.koltunm.skanner.util.Log;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.javafx.application.PlatformImpl;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Unit test for simple App.
 */
public class AppTest //extends ApplicationTest
{
    private static final String defaultConfigFile = "skanner.properties";

    private Skanner skanner;
    private Properties config;
    private YahooClient yahooClient;
    private NewsClient newsClient;
    private ArticleFetcher articleFetcher;
    private TwitterFetcher twitterFetcher;
    private StockDataFetcher stockDataFetcher;
    private FetcherForm fetcherForm;

    private List<Article> articleList;
    private List<Tweet> tweetList;
    private List<StockQuote> stockQuoteList;

    private void initModules()
    {
        try
        {
            PlatformImpl.startup(()->{});
            config = Config.init(defaultConfigFile);
            skanner = Skanner.init();
            Log.init(skanner.getClass().getPackage().getName(), Config.getProperty("logs.path"));

            LocalDate to = LocalDate.now();
            LocalDate from = to.minusDays(10);
            fetcherForm = new FetcherForm(from, to, 5, 5, "AMAZON OR AWS", "AMZN", 2);
            skanner.setFetcherForm(fetcherForm);
            articleFetcher = new ArticleFetcher(fetcherForm);
            twitterFetcher = new TwitterFetcher(fetcherForm);
            stockDataFetcher = new StockDataFetcher(fetcherForm, FinanceInterval.ONE_DAY);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception
    {
        initModules();
    }

    @After
    public void tearDown() throws Exception
    {
        /*
        skanner = null;
        config = null;
         */
    }

    /*
    @Test
    public void testAppInit()
    {
        initModules();
        assertNotNull(config);
        assertNotNull(skanner);
    }
     */

    @Test
    public void testNewsClient()
    {
        newsClient = new NewsClient();
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(2);

        List<Article> articleList = newsClient.getArticles("AMAZON", from, to, 10);
        assertTrue(articleList.size() > 0);
        assertNotNull(articleList.get(0));
    }

    @Test
    public void testFinanceClient()
    {
        yahooClient = new YahooClient();
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(10);
        List<StockQuote> list = null;
        try
        {
            list = yahooClient.getHistoricalStockData("AMZN", from, to, FinanceInterval.ONE_DAY);
        }
        catch (UnirestException e)
        {
            e.printStackTrace();
        }
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testSkannerTweetFetcher()
    {
        //initModules();
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(10);
        UIAction uiAction = new UIAction()
        {
            @Override
            public void onComplete()
            {
                assertTrue(true);
            }
        };
        ServiceCallBack callBack = new ServiceCallBack(uiAction);
        Service service = skanner.fetchTweets(callBack);
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                assertNotNull(skanner.getTweetList());
                assertTrue(skanner.getTweetList().size() > 0);
            }
        });
        assertNotNull(service);
        //waitForService(service);
    }
    @Test
    public void testSkannerArticleFetcher()
    {
        //initModules();
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(10);
        FetcherForm form = new FetcherForm(from, to, 5, 5, "AMAZON OR AWS", "AMZN", 2);
        skanner.setFetcherForm(form);
        UIAction uiAction = new UIAction()
        {
            @Override
            public void onComplete()
            {
                assertTrue(true);
            }
        };
        ServiceCallBack callBack = new ServiceCallBack(uiAction);
        Service service = skanner.fetchArticles(callBack);

        service.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                assertNotNull(skanner.getArticleList());
                assertTrue(skanner.getArticleList().size() > 0);
            }
        });
        assertNotNull(service);
        //service.start();
        //waitForService(service);
    }

    @Test
    public void testSkannerStockFetcher()
    {
        //initModules();
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(10);
        FetcherForm form = new FetcherForm(from, to, 5, 5, "AMAZON OR AWS", "AMZN", 2);
        skanner.setFetcherForm(form);
        UIAction uiAction = new UIAction()
        {
            @Override
            public void onComplete()
            {
                assertTrue(true);
            }
        };
        ServiceCallBack callBack = new ServiceCallBack(uiAction);

        Service service = skanner.fetchStockData(callBack);

        service.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                assertNotNull(skanner.getStockQuoteList());
                assertTrue(skanner.getStockQuoteList().size() > 0);
            }
        });
        assertNotNull(service);
        //service.start();
        //waitForService(service);
    }

    @Test
    public void testSentimentAnalysis()
    {
        String text1 = "When Game of Thrones debuted in 2011, it garnered an average of 9.3 million " +
                "audiences. And in the seventh season, the average viewership went up to 32.8 million. Very few series " +
                "have increased viewership every season, with AMC’s Breaking Bad, aired between 2008 and 2013, being " +
                "an exception. So, what are the reasons for Game of Thrones’ success? First of all, let’s admit it has a " +
                "fantastic content line-up, and is undoubtedly a quality show. Otherwise, why would this series be " +
                "nominated for a whopping 132 Emmy Awards and eventually win 47, way more than any other drama series. " +
                "The much-liked series has already won the Emmy Award for Best Drama three times. The series, by the way, " +
                "is readily available. And that increases the viewership. While nearly 10.3 million watch Game of Thrones " +
                "on HBO, around 22.5 million watch the show on streaming sites. Game of Thrones also has limited episodes " +
                "compared to other series. Thus, it becomes easier for those who haven’t seen the earlier episodes to " +
                "catch up while it was on pause, and help boost viewer count. Game of Thrones is expected to air only 73 " +
                "episodes over the eight seasons, compared to Big Bang Theory’s 72 episodes in the past three-year period " +
                "alone. Last but not the least, for the final season, HBO said that the drama will be seen in at least " +
                "207 countries and territories, which is expected to generate over one billion viewers across the globe.";

        Analyser analyser = new Analyser();
        OverallScore score = analyser.getSentimentScore(text1);
        assertTrue(score.getType() == SentimentType.NEGATIVE);
    }

    @Test
    public void testMachineLearning()
    {
        DataSet dataSet = new DataSet(Config.getProperty("app.datasetFileName"));
        MachineLearning model = new LinearRegressionModel();
        dataSet.removeAttributes();
        PredictionResult result = null;
        try
        {
            result = model.predict(dataSet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        assertNotNull(result.getPriceChange());
    }

    @Test
    public void testDataSetBuilder()
    {
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(10);
        yahooClient = new YahooClient();
        newsClient = new NewsClient();
        List<Article> articleList = null;
        List<StockQuote> stockQuoteList = null;
        List<Tweet> tweetList = null;

    }
}
