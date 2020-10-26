package com.koltunm.skanner.clients.finance;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class StockQuote
{
    private BigDecimal open, close, low, high, adjClose;
    private Long volume;
    private LocalDate date;

    public StockQuote(BigDecimal open, BigDecimal close, BigDecimal low, BigDecimal high, BigDecimal adjClose, Long volume, LocalDate date)
    {
        this.open = open;
        this.close = close;
        this.low = low;
        this.high = high;
        this.adjClose = adjClose;
        this.volume = volume;
        this.date = date;
    }

    public String toString()
    {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return String.format("%-15s OPEN: %.2f CLOSE: %.2f HIGH: %.2f LOW: %.2f",
                date.toString(), open, close, high, low);
    }

    public BigDecimal getOpen()
    {
        return open;
    }

    public void setOpen(BigDecimal open)
    {
        this.open = open;
    }

    public BigDecimal getClose()
    {
        return close;
    }

    public void setClose(BigDecimal close)
    {
        this.close = close;
    }

    public BigDecimal getLow()
    {
        return low;
    }

    public void setLow(BigDecimal low)
    {
        this.low = low;
    }

    public BigDecimal getHigh()
    {
        return high;
    }

    public void setHigh(BigDecimal high)
    {
        this.high = high;
    }

    public BigDecimal getAdjClose()
    {
        return adjClose;
    }

    public void setAdjClose(BigDecimal adjClose)
    {
        this.adjClose = adjClose;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public Long getVolume()
    {
        return volume;
    }

    public void setVolume(Long volume)
    {
        this.volume = volume;
    }
}
