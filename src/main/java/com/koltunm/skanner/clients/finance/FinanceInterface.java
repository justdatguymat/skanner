package com.koltunm.skanner.clients.finance;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.time.LocalDate;
import java.util.List;

public interface FinanceInterface
{
    public List<StockQuote> getHistoricalStockData(
            String tickerSymbol,
            LocalDate from,
            LocalDate to,
            FinanceInterval interval) throws UnirestException;
}
