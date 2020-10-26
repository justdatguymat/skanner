package com.koltunm.skanner.clients.news;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.koltunm.skanner.clients.AbstractClient;
import com.koltunm.skanner.util.Log;
import com.mashape.unirest.request.GetRequest;
import org.json.JSONObject;
import org.json.JSONArray;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class NewsClient extends AbstractClient implements NewsInterface
{
    private String apiVersion;

    public NewsClient()
    {
        super("news");
        apiVersion = config.getProperty("news.apiVersion");
    }

    private GetRequest getBaseRequest(String url)
    {
        final GetRequest getRequest = (GetRequest) Unirest.get(url)
                .header("accept", "application/json")
                .header("X-Api-Key", apiKey)
                .queryString("region", "US")
                .queryString("lang", "en")
                .queryString("language", "en");
        Log.i("Building a request: " + getRequest.toString());
        return getRequest;
    }

    private JSONObject makeRequest(String url, String query, LocalDate from, LocalDate to, int pageSize) throws UnirestException
    {
        Log.i(String.format("Making a request: [%s][%s][%s][%s][%d]", url, query, from, to, pageSize));
        HttpResponse<JsonNode> response = getBaseRequest(url)
                .queryString("q", query)
                .queryString("sortBy", "popularity")
                .queryString("from", from)
                .queryString("to", to)
                .queryString("pageSize", pageSize)
                //.queryString("category", "business")// FIXME category doesn't work with EVERYTHING endpoint
                .asJson();

        JSONObject jo = response.getBody().getObject();
        validateResponse(response, jo, "message");
        return jo;
    }

    public List<Article> getTopHeadlines(String query, LocalDate from, LocalDate to, int pageSize) throws UnirestException
    {
        JSONObject body = makeRequest(constructUrl(String.format("/%s/top-headlines", apiVersion)), query, from, to, pageSize);
        return bodyToArticles(body);
    }

    public List<Article> getEverything(String query, LocalDate from, LocalDate to, int pageSize) throws UnirestException
    {
        JSONObject body = makeRequest(constructUrl(String.format("/%s/everything", apiVersion)), query, from, to, pageSize);
        return bodyToArticles(body);
    }

    public List<ArticleSource> getSources() throws UnirestException
    {
        // TODO
        return null;
    }

    private List<Article> bodyToArticles(JSONObject jo)
    {
        List<Article> articleList = new ArrayList<>();
        JSONArray jaArticles = jo.getJSONArray("articles");
        for (int i = 0; i < jaArticles.length(); i++)
        {
            JSONObject joArticle = jaArticles.getJSONObject(i);
            //Log.i(joArticle);
            Article tmp = new Article(joArticle);
            //Log.i(tmp);
            articleList.add(tmp);
        }
        return articleList;
    }

    public List<Article> getArticles(String query, LocalDate from, LocalDate to, int pageSize)
    {
        List<Article> articles = null;
        try
        {
            articles = getTopHeadlines(query, from, to, pageSize);
            articles.addAll(getEverything(query, from, to, pageSize));
        }
        catch (UnirestException e)
        {
            Log.e("Getting articles had failed.", e);
        }
        return articles;
    }
}
