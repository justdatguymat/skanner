package com.koltunm.skanner.clients.twitter;

public class RequestResetter implements Runnable
{
    final private RequestLimiter limiter;

    public RequestResetter(RequestLimiter limiter)
    {
        this.limiter = limiter;
    }

    @Override
    public void run()
    {
        int count = 0;
        while (true)
        {
            count++;
            if (count >= 60)
            {
                limiter.setRequestCountMin(0);
                count = 0;
            }
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            limiter.setRequestCountSec(0);
        }
    }
}
