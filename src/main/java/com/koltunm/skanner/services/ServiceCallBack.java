package com.koltunm.skanner.services;

import com.koltunm.skanner.ui.UIAction;
import com.koltunm.skanner.util.Log;

public class ServiceCallBack
{
    private int runningServices;
    private UIAction action;

    public ServiceCallBack(UIAction action)
    {
        runningServices = 0;
        this.action = action;
    }

    public void registerService()
    {
        incrementServiceCount();
    }

    public void worksDone()
    {
        decrementServiceCount();
        Log.i(String.format("Works done, decrementing running service count: %d", runningServices));
        if (isComplete())
        {
            Log.i("Action completed, incoking callback function!");
            action.onComplete();
        }
    }

    private synchronized boolean isComplete()
    {
        return runningServices == 0;
    }

    private synchronized void incrementServiceCount()
    {
        runningServices++;
    }

    private synchronized void decrementServiceCount()
    {
        runningServices--;
    }
}
