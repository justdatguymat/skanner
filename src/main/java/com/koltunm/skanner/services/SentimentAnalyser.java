package com.koltunm.skanner.services;

import com.koltunm.skanner.sentiment.Analyser;
import com.koltunm.skanner.sentiment.AnalyserTask;
import com.koltunm.skanner.sentiment.SentimentAnalyzable;
import com.koltunm.skanner.util.Log;
import javafx.concurrent.Task;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SentimentAnalyser extends Task<List<SentimentAnalyzable>>
{
    final private List<SentimentAnalyzable> list;
    final private ThreadPoolExecutor threadPoolExecutor;

    public SentimentAnalyser(List<SentimentAnalyzable> list)
    {
        this.list = Collections.synchronizedList(list);
        int numOfProcessors = Runtime.getRuntime().availableProcessors();
        Log.i(numOfProcessors);
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfProcessors);
    }

    @Override
    protected List<SentimentAnalyzable> call()
    {
        String msg = "Analysing sentiment of tweets and articles.";
        Log.i(msg);
        updateMessage(msg);

        ConcurrentLinkedQueue<SentimentAnalyzable> queue = new ConcurrentLinkedQueue<>(list);

        Analyser analyser = new Analyser();
        for (int i = 0; i < threadPoolExecutor.getCorePoolSize(); i++)
        {
            Log.i(String.format("Starting Analayser Task %d", i));
            threadPoolExecutor.execute(new AnalyserTask(queue, analyser));
        }

        int progress;
        while (queue.peek() != null || threadPoolExecutor.getActiveCount() > 0)
        {
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            progress = list.size() - queue.size();
            updateProgress(progress, list.size());
            updateMessage(String.format("Analysed %d out of %d articles and tweets.", progress, list.size()));
        }

        Log.i(String.format("Sentiment analyzer finished : %d", queue.size()));

        threadPoolExecutor.shutdown();
        msg = String.format("Analased %d articles and tweets in total.", list.size());
        Log.i(msg);
        updateProgress(1, 1);
        updateValue(list);
        updateMessage(msg);
        return list;
    }
}
