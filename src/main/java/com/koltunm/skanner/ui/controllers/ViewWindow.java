package com.koltunm.skanner.ui.controllers;

import com.koltunm.skanner.machinelearning.DataRow;
import com.koltunm.skanner.services.FetcherForm;
import com.koltunm.skanner.clients.twitter.Tweet;
import com.koltunm.skanner.clients.finance.StockQuote;
import com.koltunm.skanner.clients.news.Article;
import com.koltunm.skanner.sentiment.SentimentAnalyzable;
import com.koltunm.skanner.sentiment.SentimentType;
import com.koltunm.skanner.ui.components.ArticleFeed;
import com.koltunm.skanner.ui.components.TweetFeed;
import com.koltunm.skanner.util.Log;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewWindow extends WindowPane
{
    private final String FXML_FILE = "../../../../../fxmls/view_window.fxml";

    @FXML public TableView tableStock;
    @FXML public TableView tableDataset;
    @FXML public TableView tableAnalysis;
    @FXML public VBox boxCharts;
    //@FXML public VBox boxArticles;
    //@FXML public VBox boxTweets;
    @FXML public VBox boxViewContainer;
    @FXML public ListView<ArticleFeed> listArticles;
    @FXML public ListView<TweetFeed> listTweets;
    @FXML public Button btnRefresh;
    @FXML public LineChart lineChart;
    @FXML public ScrollPane scrollPaneTweets;
    @FXML public ScrollPane scrollPaneArticles;

    private ObservableList<ArticleFeed> articleBuffer;
    private ObservableList<ArticleFeed> articleBigData;
    private ObservableList<TweetFeed> tweetBuffer;
    private ObservableList<TweetFeed> tweetBigData;

    private int feedArticleStart = 0;
    private int feedTweetStart = 0;
    private int feedStep = 10;

    public ViewWindow(MenuWindow menuWindow)
    {
        super();
        init(menuWindow, FXML_FILE);
        articleBigData = FXCollections.observableArrayList();
        articleBuffer = FXCollections.observableArrayList();
        tweetBigData = FXCollections.observableArrayList();
        tweetBuffer = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initStockTable();
        initAnalysisTable();
        initDatasetTable();
    }

    private void initStockTable()
    {
        TableColumn<LocalDate, StockQuote> columnDate = new TableColumn<>("Date");
        TableColumn<BigDecimal, StockQuote> columnOpen = new TableColumn<>("Open");
        TableColumn<BigDecimal, StockQuote> columnLow = new TableColumn<>("Low");
        TableColumn<BigDecimal, StockQuote> columnHigh = new TableColumn<>("High");
        TableColumn<BigDecimal, StockQuote> columnClose = new TableColumn<>("Close");
        TableColumn<BigDecimal, StockQuote> columnAdjClose = new TableColumn<>("Adjusted Close");
        TableColumn<Long, StockQuote> columnVolume = new TableColumn<>("Volume");

        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnOpen.setCellValueFactory(new PropertyValueFactory<>("open"));
        columnLow.setCellValueFactory(new PropertyValueFactory<>("low"));
        columnHigh.setCellValueFactory(new PropertyValueFactory<>("high"));
        columnClose.setCellValueFactory(new PropertyValueFactory<>("close"));
        columnAdjClose.setCellValueFactory(new PropertyValueFactory<>("adjClose"));
        columnVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));

        tableStock.getColumns().add(columnDate);
        tableStock.getColumns().add(columnOpen);
        tableStock.getColumns().add(columnLow);
        tableStock.getColumns().add(columnHigh);
        tableStock.getColumns().add(columnClose);
        tableStock.getColumns().add(columnAdjClose);
        tableStock.getColumns().add(columnVolume);
    }

    private void initAnalysisTable()
    {
        TableColumn<LocalDate, SentimentAnalyzable> columnDate = new TableColumn<>("DateTime");
        TableColumn<String, SentimentAnalyzable> columnTitle = new TableColumn<>("Article/Tweet");
        TableColumn<SentimentType, SentimentAnalyzable> columnSentimentType = new TableColumn<>("Sentiment");
        TableColumn<Integer, SentimentAnalyzable> columnVeryNegative = new TableColumn<>("Very negative");
        TableColumn<Integer, SentimentAnalyzable> columnNegative = new TableColumn<>("Negative");
        TableColumn<Integer, SentimentAnalyzable> columnNeutral = new TableColumn<>("Neutral");
        TableColumn<Integer, SentimentAnalyzable> columnPositive = new TableColumn<>("Positive");
        TableColumn<Integer, SentimentAnalyzable> columnVeryPositive = new TableColumn<>("Very positive");

        columnDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("sentimentHeading"));
        columnSentimentType.setCellValueFactory(new PropertyValueFactory<>("sentimentType"));
        columnVeryNegative.setCellValueFactory(new PropertyValueFactory<>("veryNegative"));
        columnNegative.setCellValueFactory(new PropertyValueFactory<>("negative"));
        columnNeutral.setCellValueFactory(new PropertyValueFactory<>("neutral"));
        columnPositive.setCellValueFactory(new PropertyValueFactory<>("positive"));
        columnVeryPositive.setCellValueFactory(new PropertyValueFactory<>("veryPositive"));

        tableAnalysis.getColumns().add(columnDate);
        tableAnalysis.getColumns().add(columnTitle);
        tableAnalysis.getColumns().add(columnSentimentType);
        tableAnalysis.getColumns().add(columnVeryNegative);
        tableAnalysis.getColumns().add(columnNegative);
        tableAnalysis.getColumns().add(columnNeutral);
        tableAnalysis.getColumns().add(columnPositive);
        tableAnalysis.getColumns().add(columnVeryPositive);
    }

    private void initDatasetTable()
    {
        TableColumn<LocalDate, DataRow> columnDate = new TableColumn<>("Date");
        TableColumn<Integer, DataRow> columnPositives = new TableColumn<>("Positives");
        TableColumn<Integer, DataRow> columnNeutrals = new TableColumn<>("Neutrals");
        TableColumn<Integer, DataRow> columnNegatives = new TableColumn<>("Negatives");
        TableColumn<Integer, DataRow> columnTotalTweets = new TableColumn<>("Total tweets");
        TableColumn<Integer, DataRow> columnTotalArticles = new TableColumn<>("Total articles");
        TableColumn<Long, DataRow> columnVolume = new TableColumn<>("Volume");
        TableColumn<Double, DataRow> columnLowPrice = new TableColumn<>("Low price");
        TableColumn<Double, DataRow> columnHighPrice = new TableColumn<>("High price");
        TableColumn<Double, DataRow> columnOpenPrice = new TableColumn<>("Open price");
        TableColumn<Double, DataRow> columnClosePrice = new TableColumn<>("Close price");
        TableColumn<Double, DataRow> columnChangePrice = new TableColumn<>("Price change");

        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnPositives.setCellValueFactory(new PropertyValueFactory<>("positives"));
        columnNeutrals.setCellValueFactory(new PropertyValueFactory<>("neutrals"));
        columnNegatives.setCellValueFactory(new PropertyValueFactory<>("negatives"));
        columnTotalTweets.setCellValueFactory(new PropertyValueFactory<>("totalTweets"));
        columnTotalArticles.setCellValueFactory(new PropertyValueFactory<>("totalArticles"));
        columnVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        columnLowPrice.setCellValueFactory(new PropertyValueFactory<>("lowPrice"));
        columnHighPrice.setCellValueFactory(new PropertyValueFactory<>("highPrice"));
        columnOpenPrice.setCellValueFactory(new PropertyValueFactory<>("openPrice"));
        columnClosePrice.setCellValueFactory(new PropertyValueFactory<>("closePrice"));
        columnChangePrice.setCellValueFactory(new PropertyValueFactory<>("changePrice"));

        tableDataset.getColumns().add(columnDate);
        tableDataset.getColumns().add(columnPositives);
        tableDataset.getColumns().add(columnNeutrals);
        tableDataset.getColumns().add(columnNegatives);
        tableDataset.getColumns().add(columnTotalTweets);
        tableDataset.getColumns().add(columnTotalArticles);
        tableDataset.getColumns().add(columnVolume);
        tableDataset.getColumns().add(columnLowPrice);
        tableDataset.getColumns().add(columnHighPrice);
        tableDataset.getColumns().add(columnOpenPrice);
        tableDataset.getColumns().add(columnClosePrice);
        tableDataset.getColumns().add(columnChangePrice);
    }

    /*
    Ref: https://www.superglobals.net/javafx-listview-lazy-loading/
    Used for reference but made small modifications and improvements
     */
    private void initTweetListViews()
    {
        ScrollBar tweetScrollBar = getListViewScrollBar(listTweets);
        tweetScrollBar.valueProperty().addListener((observable, oldValue, newValue ) -> {
            double positiion = newValue.doubleValue();
            ScrollBar scrollBar = getListViewScrollBar(listTweets);
            if (positiion == scrollBar.getMax())
            {
                int tmpStep = feedStep + feedTweetStart;
                if (tweetBigData.size() <= tmpStep)
                {
                    tmpStep = tweetBigData.size() - 1;
                }
                if (feedTweetStart < tweetBigData.size())
                {
                    tweetBuffer.addAll(tweetBigData.subList(feedTweetStart, tmpStep));
                    feedTweetStart += tmpStep;
                    listTweets.setItems(tweetBuffer);
                    tweetScrollBar.setValue(oldValue.doubleValue());
                }
            }
        });
    }

    private void initArticleListViews()
    {
        ScrollBar articleScrollBar = getListViewScrollBar(listArticles);
        articleScrollBar.valueProperty().addListener((observable, oldValue, newValue ) -> {
            double positiion = newValue.doubleValue();
            ScrollBar scrollBar = getListViewScrollBar(listArticles);
            if (positiion == scrollBar.getMax())
            {
                int tmpStep = feedStep + feedArticleStart;
                if (articleBigData.size() <= tmpStep)
                {
                    tmpStep = articleBigData.size() - 1;
                }
                if (feedArticleStart < articleBigData.size())
                {
                    List<ArticleFeed> arts = articleBigData.subList(feedArticleStart, tmpStep);
                    articleBuffer.addAll(arts);
                    feedArticleStart += tmpStep;
                    listArticles.setItems(articleBuffer);
                    loadArticleImages(arts);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            articleScrollBar.setValue(oldValue.doubleValue());
                        }
                    });
                }
            }
        });
    }

    private void loadArticleImages(List<ArticleFeed> articles)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                Article a = null;
                for (ArticleFeed af : articles)
                {
                    a = af.getArticle();
                    if (a.getImage() == null)
                    {
                        a.loadImage();
                        a.getArticleFeed().setImage(a.getImage());
                    }
                }
            }
        });
    }

    @Override
    public void reset()
    {
    }

    @Override
    public void open()
    {
        super.open();
        Platform.runLater(() -> loadData());
    }

    public void actionRefresh(ActionEvent actionEvent)
    {
        Platform.runLater(() -> loadData());
    }

    private void loadData()
    {
        if (!skanner.isDataFetched())
        {
            Log.w("Skanner claims there's no data fetched.");
            return;
        }
        setStockTableData();
        setArticleFeed();
        setTweetFeed();
        setLineChart();

        if (skanner.isAnalysisCompleted())
        {
            setAnalysisTable();
            setDatasetTable();
        }
    }

    private void setDatasetTable()
    {
        tableDataset.getItems().removeAll();
        tableDataset.getItems().addAll(skanner.getDataRowList());
    }

    private void setStockTableData()
    {
        tableStock.getItems().removeAll();
        tableStock.getItems().addAll(skanner.getStockQuoteList());
    }

    private void setAnalysisTable()
    {
        tableAnalysis.getItems().removeAll();
        tableAnalysis.getItems().addAll(skanner.getSentimentAnalyzableList());
    }

    private void setArticleFeed()
    {
        articleBigData = FXCollections.observableArrayList(
                skanner.getArticleList().stream().map(Article::getArticleFeed).collect(Collectors.toList())
        );
        int till = feedArticleStart + feedStep;
        if (till >= articleBigData.size())
        {
            till = articleBigData.size() - 1;
        }
        if (feedArticleStart < articleBigData.size())
        {
            articleBuffer = FXCollections.observableArrayList(
                    articleBigData.subList(feedArticleStart, till)
            );
            listArticles.setItems(articleBuffer);
            feedArticleStart += till;
            loadArticleImages(articleBuffer);

            initArticleListViews();
        }
    }

    private void setTweetFeed()
    {
        tweetBigData = FXCollections.observableArrayList(
                skanner.getTweetList().stream().map(Tweet::getTweetFeed).collect(Collectors.toList())
        );
        int till = feedTweetStart + feedStep;
        if (till >= tweetBigData.size())
        {
            till = tweetBigData.size() - 1;
        }
        if (feedTweetStart < tweetBigData.size())
        {
            tweetBuffer = FXCollections.observableArrayList(tweetBigData.subList(feedTweetStart, till));
            listTweets.setItems(tweetBuffer);
            feedTweetStart += feedStep;
            initTweetListViews();
        }
    }

    private void setLineChart()
    {
        boxCharts.getChildren().remove(lineChart);

        FetcherForm fetcherForm = skanner.getFetcherForm();

        XYChart.Series series = new XYChart.Series();
        NumberAxis priceAx = new NumberAxis();
        CategoryAxis dateAx = new CategoryAxis();
        priceAx.setForceZeroInRange(false);
        priceAx.setTickUnit(5.0);
        lineChart = new LineChart(dateAx, priceAx);

        for (StockQuote hq : skanner.getStockQuoteList())
        {
            series.getData().add(new XYChart.Data(hq.getDate().toString(), hq.getClose()));
        }

        series.setName(fetcherForm.getTickerSymbol());
        lineChart.getData().addAll(series);
        lineChart.setCursor(Cursor.CROSSHAIR);
        lineChart.setTitle(String.format("Historical quota between %s and %s", fetcherForm.getFrom(), fetcherForm.getTo()));
        boxCharts.getChildren().add(lineChart);
    }

    /*
    Ref: https://www.superglobals.net/javafx-listview-lazy-loading/
     */
    private ScrollBar getListViewScrollBar(ListView<?> listView)
    {
        ScrollBar scrollBar = null;
        for (Node node : listView.lookupAll(".scroll-bar"))
        {
            if (node instanceof ScrollBar)
            {
                ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation().equals(Orientation.VERTICAL))
                {
                    scrollBar = bar;
                }
            }
        }
        return scrollBar;
    }
}
