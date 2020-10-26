package com.koltunm.skanner.services;

import java.time.LocalDate;

public class FetcherForm
{
    final private LocalDate from, to;
    final private int interval, pageSize, groupingRange;
    final private String query, tickerSymbol;

    public FetcherForm(LocalDate from, LocalDate to, int interval, int pageSize, String query, String tickerSymbol, int groupingRange)
    {
        this.from = from;
        this.to = to;
        this.interval = interval;
        this.pageSize = pageSize;
        this.query = query;
        this.tickerSymbol = tickerSymbol;
        this.groupingRange = groupingRange;
    }

    public LocalDate getFrom()
    {
        return from;
    }

    public LocalDate getTo()
    {
        return to;
    }

    public int getInterval()
    {
        return interval;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public String getQuery()
    {
        return query;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public int getGroupingRange()
    {
        return groupingRange;
    }
}
