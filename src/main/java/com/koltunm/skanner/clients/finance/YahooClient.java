package com.koltunm.skanner.clients.finance;

import com.koltunm.skanner.clients.AbstractClient;
import com.koltunm.skanner.clients.news.Article;
import com.koltunm.skanner.clients.news.NewsInterface;
import com.koltunm.skanner.util.Log;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class YahooClient extends AbstractClient implements FinanceInterface, NewsInterface
{

    public YahooClient()
    {
        super("finance");
    }

    private GetRequest getBaseRequest(String url)
    {
        final GetRequest getRequest = (GetRequest) Unirest.get(url)
                .header("X-RapidAPI-Host", host)
                .header("X-RapidAPI-Key", apiKey)
                .queryString("region", "US")
                .queryString("lang", "en");
        Log.i("Building a request: " + getRequest.toString());
        return getRequest;
    }

    public List<StockQuote> getHistoricalStockData(
            String tickerSymbol, LocalDate from, LocalDate to, FinanceInterval interval)
            throws UnirestException
    {
        /*
        "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/get-histories
                 ?region=US &lang=en &symbol=AMZN &from=1547524844 &to=1555463510 &events=div &interval=1d"
         */

        HttpResponse<JsonNode> response = getBaseRequest(constructUrl("/stock/get-histories"))
                .queryString("symbol", tickerSymbol)
                .queryString("events", FinanceStockEvent.DIVIDENDS.event)
                .queryString("events", FinanceStockEvent.SPLITS.event)
                .queryString("events", FinanceStockEvent.EARNINGS.event)
                .queryString("interval", interval)
                .queryString("from", from.toEpochDay() * 24 * 60 * 60)
                .queryString("to", to.toEpochDay() * 24 * 60 * 60)
                .asJson();
        JSONObject jo = response.getBody().getObject();
        //Log.i(jo.toString(4));
        return jsonToHistoricalQuote(jo);
    }

    private List<StockQuote> jsonToHistoricalQuote(JSONObject jo)
    {
        List<StockQuote> hqList = new ArrayList<>();
        JSONObject result = jo.getJSONObject("chart").getJSONArray("result").getJSONObject(0);
        //Log.i(result.toString(4));
        //JSONObject meta = result.getJSONObject("meta");
        //Log.i(meta.toString(4));
        JSONArray timestamps = result.getJSONArray("timestamp");
        JSONObject indicators = result.getJSONObject("indicators").getJSONArray("quote").getJSONObject(0);
        JSONArray low = indicators.getJSONArray("low");
        JSONArray high = indicators.getJSONArray("high");
        JSONArray open = indicators.getJSONArray("open");
        JSONArray close = indicators.getJSONArray("close");
        JSONArray volume = indicators.getJSONArray("volume");
        JSONArray adjClose = result.getJSONObject("indicators").getJSONArray("adjclose").getJSONObject(0).getJSONArray("adjclose");

        if (timestamps.length() == volume.length()
            && timestamps.length() == low.length()
            && timestamps.length() == high.length()
            && timestamps.length() == close.length()
            && timestamps.length() == open.length()
            && timestamps.length() == adjClose.length())
        {
            Log.i("Length of arrays match!");
        }
        else
        {
            Log.w("Length of arrays DON'T match!");
        }

        for (int i = 0; i < timestamps.length(); i++)
        {
            Instant instant = Instant.ofEpochSecond(timestamps.getLong(i));
            LocalDate date  = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            BigDecimal tempOpen = open.getBigDecimal(i);
            BigDecimal tempClose = close.getBigDecimal(i);
            BigDecimal tempLow = low.getBigDecimal(i);
            BigDecimal tempHigh = high.getBigDecimal(i);
            BigDecimal tempAdjClose = adjClose.getBigDecimal(i);
            Long tempVolume = volume.getLong(i);
            StockQuote tmpHQ = new StockQuote(tempOpen, tempClose, tempLow, tempHigh, tempAdjClose, tempVolume, date);
            //Log.i(tmpHQ);
            hqList.add(tmpHQ);
        }
        return hqList;
    }

    public List<Article> getArticles(String tickerSymbol, LocalDate from, LocalDate to, int pageSize)
    {
        HttpResponse<JsonNode> response = null;
        try
        {
            response = getBaseRequest(constructUrl("/stock/get-news"))
                    .queryString("category", tickerSymbol)
                    .asJson();
        }
        catch (UnirestException e)
        {
            e.printStackTrace();
            Log.e("Getting articles had failed.", e);
        }
        JSONObject jo = response.getBody().getObject();
        Log.i(jo.toString(4));
        // TODO: convert JO to article objects
        return null;
    }
}
