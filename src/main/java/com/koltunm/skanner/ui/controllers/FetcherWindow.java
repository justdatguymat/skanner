package com.koltunm.skanner.ui.controllers;

import com.koltunm.skanner.services.FetcherForm;
import com.koltunm.skanner.clients.twitter.Tweet;
import com.koltunm.skanner.clients.finance.StockQuote;
import com.koltunm.skanner.clients.news.Article;
import com.koltunm.skanner.services.ServiceCallBack;
import com.koltunm.skanner.ui.UIAction;
import com.koltunm.skanner.util.Config;
import com.koltunm.skanner.util.Log;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;


public class FetcherWindow extends WindowPane implements UIAction
{
    private final String FXML_FILE = "../../../../../fxmls/fetcher_window.fxml";
    private enum State
    {
        FETCHING, STANDBY;
    }

    @FXML public Button btnBack;
    @FXML public Button btnFetchCancel;
    @FXML public Button btnProceed;
    @FXML public DatePicker dpFrom;
    @FXML public DatePicker dpTo;
    @FXML public HBox boxArticleFetcher;
    @FXML public HBox boxStockFetcher;
    @FXML public HBox boxTwitterFetcher;
    @FXML public Label lblArticleFetcher;
    @FXML public Label lblStockFetcher;
    @FXML public Label lblTwitterFetcher;
    @FXML public ProgressIndicator pbArticleFetcher;
    @FXML public ProgressIndicator pbStockFetcher;
    @FXML public ProgressIndicator pbTwitterFetcher;
    @FXML public Slider sliderInterval;
    @FXML public Slider sliderPageSize;
    @FXML public Slider sliderGrouping;
    @FXML public TextField txtQuery;
    @FXML public TextField txtTickerSymbol;

    private State state;

    public FetcherWindow(MenuWindow menuWindow)
    {
        super();
        state = State.STANDBY;
        init(menuWindow, FXML_FILE);
    }

    public void reset()
    {
        boxStockFetcher.setVisible(false);
        boxArticleFetcher.setVisible(false);
        boxTwitterFetcher.setVisible(false);
        btnProceed.setDisable(true);
        btnFetchCancel.setDisable(false);
        btnFetchCancel.setText("Fetch");
    }

    public void initialize(URL location, ResourceBundle resources)
    {
        // setting slider value to whole integers.
        sliderInterval.valueProperty().addListener((observableValue, oldValue, newValue) ->
                sliderInterval.setValue(Math.round(newValue.doubleValue())));
        sliderPageSize.valueProperty().addListener((observableValue, oldValue, newValue) ->
                sliderInterval.setValue(Math.round(newValue.doubleValue())));
        sliderGrouping.valueProperty().addListener((observableValue, oldValue, newValue) ->
                sliderInterval.setValue(Math.round(newValue.doubleValue())));


        // TODO FIXME hardcoded value for testing purposes
        boolean testing = Boolean.parseBoolean(Config.getProperty("app.testing"));
        if (testing)
        {
            LocalDate date = LocalDate.now();
            txtTickerSymbol.setText("AMZN");
            txtQuery.setText("AMAZON OR AWS");
            dpFrom.setValue(date.minusDays(20));
            dpTo.setValue(date.minusDays(5));
        }
    }

    private void cancel()
    {
        state = State.STANDBY;
        btnFetchCancel.setText("Fetch");
        btnBack.setDisable(false);
        // TODO stop services.
    }

    private void fetch()
    {
        state = State.FETCHING;
        btnFetchCancel.setText("Cancel");
        btnBack.setDisable(true);

        String query = txtQuery.getText();
        String tickerSymbol = txtTickerSymbol.getText();
        LocalDate from = dpFrom.getValue();
        LocalDate to = dpTo.getValue();

        // FIXME TODO hardcoded value to limit amount of requests
        int interval = (int) sliderInterval.getValue();
        int pageSize = (int) sliderPageSize.getValue();
        int groupingRange = (int) sliderGrouping.getValue();
        //interval = (int) (to.toEpochDay() - from.toEpochDay());

        FetcherForm fetcherForm = new FetcherForm(from, to, interval, pageSize, query, tickerSymbol, groupingRange);
        skanner.setFetcherForm(fetcherForm);

        ServiceCallBack callBack = new ServiceCallBack(this);
        startFetchingArticles(callBack);
        startFetchingStockData(callBack);
        startFetchingTweets(callBack);
    }

    private void startFetchingTweets(ServiceCallBack callBack)
    {
        Service<List<Tweet>> twitterFetcher = skanner.fetchTweets(callBack);
        pbTwitterFetcher.progressProperty().bind(twitterFetcher.progressProperty());
        lblTwitterFetcher.textProperty().bind(twitterFetcher.messageProperty());
        Log.i("Starting the tweet fetcher.");
        twitterFetcher.start();
    }

    private void startFetchingStockData(ServiceCallBack callBack)
    {
        Log.i("Fetching the Stock Data. Task created inside the service.");
        Service<List<StockQuote>> stockFetcher = skanner.fetchStockData(callBack);
        pbStockFetcher.progressProperty().bind(stockFetcher.progressProperty());
        lblStockFetcher.textProperty().bind(stockFetcher.messageProperty());
        Log.i("Starting the stock fetcher.");
        stockFetcher.start();
    }

    private void startFetchingArticles(ServiceCallBack callBack)
    {
        Log.i("Creating article fetching service.");
        Service<List<Article>> articleFetcher = skanner.fetchArticles(callBack);
        pbArticleFetcher.progressProperty().bind(articleFetcher.progressProperty());
        lblArticleFetcher.textProperty().bind(articleFetcher.messageProperty());
        Log.i("starting the Article Fetcher service.");
        articleFetcher.start();
    }

    public void onComplete()
    {
        state = State.STANDBY;
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                btnBack.setDisable(false);
                if (skanner.isDataFetched())
                {
                    btnFetchCancel.setDisable(true);
                    btnProceed.setDisable(false);
                    menuWindow.btnData.setDisable(false);
                    menuWindow.btnAnalyze.setDisable(false);
                }
                else
                {
                    sendAlert(Alert.AlertType.INFORMATION, "Not enough data obtained.");
                    btnFetchCancel.setDisable(false);
                    btnFetchCancel.setText("Fetch");
                    btnProceed.setDisable(true);
                }
            }
        });
    }

    public void actionBack(ActionEvent actionEvent)
    {
        ((Stage) btnBack.getScene().getWindow()).close();
    }

    public void actionFetchCancel(ActionEvent actionEvent)
    {
        boxArticleFetcher.setVisible(true);
        boxStockFetcher.setVisible(true);
        boxTwitterFetcher.setVisible(true);
        if (state == State.STANDBY)
        {
            fetch();
        }
        else if (state == State.FETCHING)
        {
            cancel();
        }
    }

    public void actionProceed(ActionEvent actionEvent)
    {
        ((Stage) btnProceed.getScene().getWindow()).close();
    }
}
