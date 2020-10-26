package com.koltunm.skanner.clients.finance;

public class Stock
{
    private String symbol, exchangeName;

    public Stock(String symbol, String exchangeName)
    {
        this.symbol = symbol;
        this.exchangeName = exchangeName;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public String getExchangeName()
    {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName)
    {
        this.exchangeName = exchangeName;
    }
}
