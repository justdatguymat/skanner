package com.koltunm.skanner.ui.controllers;

import com.koltunm.skanner.util.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MenuWindow extends WindowPane implements Initializable
{
    private final String FXML_FILE = "../../../../../fxmls/menu_window.fxml";

    @FXML public Button btnFetch;
    @FXML public Button btnLoad;
    @FXML public Button btnData;
    @FXML public Button btnSave;
    @FXML public Button btnAnalyze;
    @FXML public Button btnPredict;

    private FetcherWindow fetcherWindow;
    private ViewWindow viewWindow;
    private AnalysisWindow analysisWindow;
    private PredictionWindow predictionWindow;

    public MenuWindow()
    {
        super();
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }
        catch (IOException exception)
        {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void reset()
    {

    }

    public void initialize(URL location, ResourceBundle resources)
    {
        fetcherWindow = new FetcherWindow(this);
        viewWindow = new ViewWindow(this);
        analysisWindow = new AnalysisWindow(this);
        predictionWindow = new PredictionWindow(this);
    }

    public void actionLoad(ActionEvent actionEvent)
    {
        // TODO LOAD DATASET
        skanner.load(Config.getProperty("app.dataSetFileName"));
        sendAlert(Alert.AlertType.INFORMATION, "Dataset had been successfully loaded.");
        btnPredict.setDisable(false);
    }

    public void actionFetch(ActionEvent actionEvent)
    {
        fetcherWindow.open();
    }

    public void actionShowData(ActionEvent actionEvent)
    {
        viewWindow.open();
    }

    public void actionAnalyze(ActionEvent actionEvent)
    {
        analysisWindow.open();
    }

    public void actionSave(ActionEvent actionEvent)
    {
        String fileName = String.format("%s_%s", Config.getProperty("app.datasetFileName"), LocalDateTime.now().toString());
        skanner.save(fileName);
        sendAlert(Alert.AlertType.INFORMATION, String.format("Dataset had been saved to %s.", fileName));
    }

    public void actionPredict(ActionEvent actionEvent)
    {
        predictionWindow.open();
    }
}
