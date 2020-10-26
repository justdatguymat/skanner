package com.koltunm.skanner.clients.finance;

public enum FinanceInterval
{
    ONE_MIN("1m"),
    TWO_MIN("2m"),
    FIVE_MIN("5m"),
    FIFTEEN_MIN("15m"),
    THIRTY_MIN("30m"),
    SIXTY_MIN("60m"),
    NINTY_MIN("90m"),
    ONE_HOUR("1h"),
    ONE_DAY("1d"),
    FIVE_DAYS("5d"),
    ONE_WEEK("1wk"),
    ONE_MONTH("1mo"),
    THREE_MONTHS("3mo"),
    FIVE_MONTHS("5mo"),
    ONE_YEAR("1y"),
    TWO_YEARS("2y"),
    FIVE_YEARS("5y"),
    TEN_YEARS("10y"),
    YEAR_TO_DATE("ytd"),
    MAX("max");

    /*
    1m, 2m, 5m, 15m, 30m, 60m, 90m, 1h, 1d, 5d, 1wk, 1mo, 3mo
    0:"1d"
    1:"5d"
    2:"1mo"
    3:"3mo"
    4:"6mo"
    5:"1y"
    6:"2y"
    7:"5y"
    8:"10y"
    9:"ytd"
    10:"max"
    */

    public final String term;

    FinanceInterval(String term)
    {
        this.term = term;
    }
}
