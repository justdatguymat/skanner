package com.koltunm.skanner.ui.controllers;

import com.koltunm.skanner.sentiment.SentimentAnalyzable;
import com.koltunm.skanner.services.ServiceCallBack;
import com.koltunm.skanner.ui.UIAction;
import com.koltunm.skanner.util.Log;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AnalysisWindow extends WindowPane implements UIAction
{
    private final String FXML_FILE = "../../../../../fxmls/analysis_window.fxml";

    @FXML public TextField txtTickerSymbol;
    @FXML public TextField txtQuery;
    @FXML public HBox boxSentiment;
    @FXML public Label lblSentiment;
    @FXML public ProgressIndicator pbSentiment;
    @FXML public Button btnBack;
    @FXML public Button btnCancelProceed;

    public AnalysisWindow(MenuWindow menuWindow)
    {
        super();
        init(menuWindow, FXML_FILE);
    }

    @Override
    public void reset()
    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    @Override
    public void open()
    {
        super.open();
        startSentimentAnalysis();
    }

    private void startSentimentAnalysis()
    {
        ServiceCallBack callBack = new ServiceCallBack(this);
        Service<List<SentimentAnalyzable>> sentimentAnalyser = skanner.analyzeSentiment(callBack);
        pbSentiment.progressProperty().bind(sentimentAnalyser.progressProperty());
        lblSentiment.textProperty().bind(sentimentAnalyser.messageProperty());
        Log.i("starting the sentiment analyser service.");
        sentimentAnalyser.start();
    }

    public void actionBack(ActionEvent actionEvent)
    {
        closeWindow();
    }

    public void actionCancelProceed(ActionEvent actionEvent)
    {
        String text = btnCancelProceed.getText();
        Log.i(String.format("Button text : %s", text));
        if (text.equals("Proceed"))
        {
            closeWindow();
        }
    }

    private void closeWindow()
    {
        ((Stage) btnBack.getScene().getWindow()).close();
    }

    @Override
    public void onComplete()
    {
        btnCancelProceed.setText("Proceed");
        menuWindow.btnSave.setDisable(false);
        menuWindow.btnPredict.setDisable(false);
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                btnCancelProceed.setText("Proceed");
            }
        });
    }
}
