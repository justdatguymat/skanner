package com.koltunm.skanner.services;

import com.koltunm.skanner.clients.finance.FinanceInterval;
import com.koltunm.skanner.clients.finance.StockQuote;
import com.koltunm.skanner.clients.finance.YahooClient;
import com.koltunm.skanner.util.Log;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;

public class StockDataFetcher extends Task<List<StockQuote>>
{
    private String tickerSymbol;
    private LocalDate from, to;
    private FinanceInterval interval;

    public StockDataFetcher(FetcherForm fetcherForm, FinanceInterval interval)
    {
        this.tickerSymbol = fetcherForm.getTickerSymbol();
        this.from = fetcherForm.getFrom();
        this.to = fetcherForm.getTo();
        this.interval = interval;
    }

    protected List<StockQuote> call()
    {
        YahooClient client = new YahooClient();
        updateMessage(String.format("Obtaining historical quotas for %s.", tickerSymbol));
        List<StockQuote> stockData = null;
        try
        {
            stockData = client.getHistoricalStockData(tickerSymbol, from, to, interval);

            BigDecimal highest = new BigDecimal(0);
            BigDecimal lowest = new BigDecimal(999999999);
            BigDecimal average = new BigDecimal(0);
            for (StockQuote hq : stockData)
            {
                if (hq.getHigh().compareTo(highest) == 1)
                {
                    highest = hq.getHigh();
                }
                if (hq.getLow().compareTo(lowest) == -1)
                {
                    lowest = hq.getLow();
                }
                average = average.add(hq.getAdjClose());
            }
            average = average.divide(BigDecimal.valueOf(stockData.size()), 2, RoundingMode.HALF_UP);
            String msg = String.format("%s quotas obtained. [ %s ][ %s ][ %s ]",
                    tickerSymbol,
                    NumberFormat.getCurrencyInstance().format(lowest),
                    NumberFormat.getCurrencyInstance().format(average),
                    NumberFormat.getCurrencyInstance().format(highest));

            Log.i(msg);
            updateMessage(msg);
            updateValue(stockData);
            updateProgress(1,1);
        }
        catch (UnirestException e)
        {
            Log.e("whoops!", e);
            e.printStackTrace();
        }
        catch (Exception e)
        {
            Log.e("whoops!", e);
            e.printStackTrace();
        }
        return stockData;
    }
}
