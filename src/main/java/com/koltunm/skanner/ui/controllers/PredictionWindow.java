package com.koltunm.skanner.ui.controllers;

import com.koltunm.skanner.clients.finance.StockQuote;
import com.koltunm.skanner.machinelearning.PredictionResult;
import com.koltunm.skanner.services.FetcherForm;
import com.koltunm.skanner.ui.UIAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PredictionWindow extends WindowPane implements UIAction
{
    private final String FXML_FILE = "../../../../../fxmls/prediction_window.fxml";

    @FXML public Button btnBack;
    @FXML public Label lblAmountChange;
    @FXML public Label lblPrice;
    @FXML public Label lblHeading;
    @FXML public Label lblPercentChange;
    @FXML public LineChart lineChart;
    @FXML public TextArea txtSummary;
    @FXML public VBox boxCharts;

    private FetcherForm fetcherForm;
    private PredictionResult predictionResult;

    public PredictionWindow(MenuWindow menuWindow)
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
        fetcherForm = skanner.getFetcherForm();
        skanner.predict(this);
        setLineChart();
    }

    private void setLineChart()
    {
        boxCharts.getChildren().remove(lineChart);

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

        series.setName("Historical");
        lineChart.setCursor(Cursor.CROSSHAIR);
        boxCharts.getChildren().add(lineChart);

        XYChart.Series prediction = new XYChart.Series();
        StockQuote sq = skanner.getStockQuoteList().get(skanner.getStockQuoteList().size() - 1);
        prediction.getData().add(new XYChart.Data(sq.getDate().toString(), sq.getClose()));

        LocalDate predictDate = getPredictionDate(fetcherForm.getTo().plusDays(1));
        prediction.getData().add(new XYChart.Data(predictDate.toString(), predictionResult.getNewPrice()));
        prediction.setName("Prediction");


        lineChart.getData().addAll(series);
        lineChart.getData().addAll(prediction);
    }

    public void actionBack(ActionEvent actionEvent)
    {
        closeWindow();
    }

    private void closeWindow()
    {
        ((Stage) btnBack.getScene().getWindow()).close();
    }

    @Override
    public void onComplete()
    {
        predictionResult = skanner.getPredictionResult();
        txtSummary.setWrapText(true);
        txtSummary.setText(predictionResult.toString());
        String sign = (predictionResult.getPriceChange() > 0) ? "+" : "";

        lblPercentChange.setText(String.format("%s%.2f%%", sign, predictionResult.getPriceChange()));
        lblAmountChange.setText(String.format("%s%.2f", sign, predictionResult.getAmountChange()));
        lblPrice.setText(String.format("%.2f", predictionResult.getNewPrice()));

        LocalDate predictDate = getPredictionDate(fetcherForm.getTo().plusDays(1));
        lblHeading.setText(String.format("Price prediction for %s on %s", fetcherForm.getTickerSymbol(), predictDate));

        if (predictionResult.getPriceChange() > 0)
        {
            lblAmountChange.setTextFill(Color.web("#007505"));
            lblPercentChange.setTextFill(Color.web("#007505"));
        }
        else
        {
            lblAmountChange.setTextFill(Color.web("#a00909"));
            lblPercentChange.setTextFill(Color.web("#a00909"));
        }
    }

    private LocalDate getPredictionDate(LocalDate date)
    {
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY)
        {
            return getPredictionDate(date.plusDays(1));
        }
        else
        {
            return date;
        }
    }
}
