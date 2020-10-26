package com.koltunm.skanner.services;

import com.koltunm.skanner.clients.news.Article;
import com.koltunm.skanner.clients.news.NewsClient;
import com.koltunm.skanner.util.Config;
import com.koltunm.skanner.util.Log;
import javafx.concurrent.Task;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ArticleFetcher extends Task<List<Article>>
{
    private NewsClient client;
    private String query;
    private LocalDate from, to;
    private int interval;
    private int pageSize;
    private boolean loadImages;

    public ArticleFetcher(FetcherForm form)
    {
        init(form.getQuery(), form.getFrom(), form.getTo(), form.getInterval(), form.getPageSize());
    }

    public ArticleFetcher(String query, LocalDate from, LocalDate to, int interval, int pageSize)
    {
        init(query, from, to, interval, pageSize);
    }

    private void init(String query, LocalDate from, LocalDate to, int interval, int pageSize)
    {
        this.query = query;
        this.from = from;
        this.to = to;
        this.interval = interval;
        this.pageSize = pageSize;
        this.client = new NewsClient();
        this.loadImages = Boolean.parseBoolean(Config.getProperty("app.loadImages"));
    }

    @Override
    protected List<Article> call()
    {
        List<Article> articles = new ArrayList<>();

        int completedWork = 0;
        int estNumArticles = (int) (from.toEpochDay() - to.toEpochDay());
        estNumArticles /= interval;
        estNumArticles *= pageSize;
        estNumArticles = Math.abs(estNumArticles);

        for (LocalDate date = from; date.isBefore(to); date = date.plusDays(interval))
        {
            LocalDate until = date.plusDays(interval);
            Log.i(String.format("Fetching articles between %s and %s.", date, until));
            updateMessage(String.format("Fetching articles between %s and %s.", date, until));

            try
            {
                Log.i("Fetching articles using the client.");
                List<Article> articleList = client.getArticles(query, date, until, pageSize);
                for (Article a : articleList)
                {
                    articles.add(a);
                    updateValue(articles);
                    updateProgress(++completedWork, estNumArticles);
                }

                if (loadImages)
                {
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (Article a : articleList)
                            {
                                a.loadImage();
                            }
                        }
                    }).start();
                }
            }
            catch (Exception e)
            {
                Log.i(e.getMessage());
                Log.e("EXCEPTION: ", e);
            }
        }
        String msg = String.format("Found %d articles in total.", articles.size());
        Log.i(msg);
        updateMessage(msg);
        updateValue(articles);
        updateProgress(1,1);

        return articles;
    }
}
