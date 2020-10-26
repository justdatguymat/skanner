package com.koltunm.skanner.services;

import com.koltunm.skanner.clients.twitter.RequestLimiter;
import com.koltunm.skanner.clients.twitter.Tweet;
import com.koltunm.skanner.util.Config;
import com.koltunm.skanner.util.Log;
import javafx.concurrent.Task;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TwitterFetcher extends Task<List<Tweet>>
{
    private Twitter twitter;
    private int tweetsPerRequest;
    private String queryString;
    private LocalDate from, to;
    private int interval;
    private RequestLimiter limiter;
    private int minLimit, secLimit;

    public TwitterFetcher(FetcherForm fetcherForm)
    {
        ConfigurationBuilder confBuilder = new ConfigurationBuilder();
        confBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(Config.getProperty("twitter.apiKey"))
                .setOAuthConsumerSecret(Config.getProperty("twitter.secretKey"))
                .setOAuthAccessToken(Config.getProperty("twitter.accessToken"))
                .setOAuthAccessTokenSecret(Config.getProperty("twitter.secretAccessToken"));
        TwitterFactory tf = new TwitterFactory(confBuilder.build());

        this.secLimit = Integer.parseInt(Config.getProperty("twitter.secLimit"));
        this.minLimit = Integer.parseInt(Config.getProperty("twitter.minLimit"));
        Log.i(String.format("Setting twitter request limitter %d and %d", secLimit, minLimit));
        this.limiter = new RequestLimiter(secLimit, minLimit);
        this.twitter = tf.getInstance();
        this.tweetsPerRequest = fetcherForm.getPageSize();
        this.queryString = fetcherForm.getQuery();
        this.from = fetcherForm.getFrom();
        this.to = fetcherForm.getTo();
        this.interval = fetcherForm.getInterval();
    }

    @Override
    protected List<Tweet> call()
    {
        List<Tweet> tweetsList = new ArrayList<>();
        updateMessage(String.format("Fetching tweets with '%s' as the query.", queryString));

        int completedWork = 0;
        int estNumTweets = (int) (from.toEpochDay() - to.toEpochDay());
        estNumTweets /= interval;
        estNumTweets *= tweetsPerRequest;
        estNumTweets = Math.abs(estNumTweets);

        for (LocalDate date = from; date.isBefore(to); date = date.plusDays(interval))
        {
            LocalDate until = date.plusDays(interval);
            String msg = String.format("Fetching tweets between %s and %s.", date, until);
            Log.i(msg);
            updateMessage(msg);

            QueryResult result = getTweets(date, until);
            for (Status status : result.getTweets())
            {
                tweetsList.add(new Tweet(status));
                //Log.i(String.format("%s - @%s : %s", status.getCreatedAt(), status.getUser().getScreenName(), status.getText()));
                updateValue(tweetsList);
                updateProgress(++completedWork, estNumTweets);
            }
        }

        String msg = String.format("Found %d tweets in total.", tweetsList.size());
        Log.i(msg);
        updateMessage(msg);
        updateValue(tweetsList);
        updateProgress(1,1);
        return tweetsList;
    }

    private QueryResult getTweets(LocalDate from, LocalDate to)
    {
        QueryResult result = null;
        try
        {
            String tmpQuery = queryString + " -filter:retweets -filter:links -filter:replies -filter:images";
            Log.i(String.format("Twitter query string :%s", tmpQuery));
            Query query = new Query(tmpQuery);
            query.setLang("en");
            query.setLocale("en");
            query.setCount(tweetsPerRequest);
            query.setSince(from.toString());
            query.setUntil(to.toString());

            // waiting for a request limiter
            Log.i("Waiting.....");
            limiter.waiting();
            result = twitter.search(query);
            Log.i(String.format("Found %d tweets.", result.getCount()));
        }
        catch (TwitterException e)
        {
            e.printStackTrace();
            Log.e("TWITTER EXCEPTION: ", e);
        }
        return result;
    }
}
