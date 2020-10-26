package com.koltunm.skanner.clients.twitter;

import com.koltunm.skanner.util.Log;

public class RequestLimiter
{
    private int requestsPerSecond;
    private int requestsPerMinute;
    private int requestCountMin;
    private int requestCountSec;

    public RequestLimiter(int requestsPerSecond, int requestsPerMinute)
    {
        this.requestCountMin = 0;
        this.requestCountSec = 0;
        this.requestsPerSecond = requestsPerSecond;
        this.requestsPerMinute = requestsPerMinute;
        new Thread(new RequestResetter(this)).start();
    }

    public void waiting()
    {
        while (getRequestCountMin() >= requestsPerMinute || getRequestCountSec() >= requestsPerSecond)
        {
            try
            {
                Log.i(String.format("MIN: %d \tSEC: %d \tGoing to sleep.", requestCountMin, requestCountSec));
                wait(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                Log.e("Request Limiter : ", e);
            }
            incrementCount();
            Log.i(String.format("MIN: %d \tSEC: %d.", requestCountMin, requestCountSec));
        }
    }

    public int getRequestsPerSecond()
    {
        return requestsPerSecond;
    }

    public int getRequestsPerMinute()
    {
        return requestsPerMinute;
    }

    public synchronized int getRequestCountMin()
    {
        return requestCountMin;
    }

    public synchronized int getRequestCountSec()
    {
        return requestCountSec;
    }

    public synchronized void setRequestCountMin(int requestCount)
    {
        this.requestCountMin = requestCount;
        Log.i("MIN notifing everyone!");
        notifyAll();
    }

    public synchronized void setRequestCountSec(int requestCount)
    {
        this.requestCountSec = requestCount;
        //Log.i("SEC Notifing everyone!");
        notifyAll();
    }

    public synchronized void incrementCount()
    {
        this.requestCountMin++;
        this.requestCountSec++;
    }
}
