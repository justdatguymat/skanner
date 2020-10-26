package com.koltunm.skanner.sentiment;

import com.koltunm.skanner.util.Log;

import java.util.concurrent.ConcurrentLinkedQueue;

public class AnalyserTask implements Runnable
{
    final private ConcurrentLinkedQueue<SentimentAnalyzable> toAnalyze;
    final private Analyser analyser;

    public AnalyserTask(ConcurrentLinkedQueue<SentimentAnalyzable> toAnalyze, Analyser analyser)
    {
        this.toAnalyze = toAnalyze;
        this.analyser = analyser;
    }

    @Override
    public void run()
    {
        SentimentAnalyzable item = null;
        while (true)
        {
            item = toAnalyze.poll();
            if (item == null)
            {
                Log.i("My job is done here!");
                break;
            }
            OverallScore score = item.getSentimentScore(analyser);
            Log.i(score);
        }
    }
}
