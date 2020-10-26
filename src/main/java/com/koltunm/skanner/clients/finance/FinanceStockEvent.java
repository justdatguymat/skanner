package com.koltunm.skanner.clients.finance;

public enum FinanceStockEvent
{
    DIVIDENDS("div"),
    EARNINGS("earn"),
    SPLITS("split");

    public final String event;

    FinanceStockEvent(String event)
    {
        this.event = event;
    }
}
