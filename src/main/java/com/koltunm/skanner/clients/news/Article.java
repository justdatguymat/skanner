package com.koltunm.skanner.clients.news;

import com.koltunm.skanner.sentiment.SentimentAnalyzable;
import com.koltunm.skanner.ui.components.ArticleFeed;
import com.koltunm.skanner.util.Log;
import javafx.scene.image.Image;
import org.json.JSONObject;
import java.time.*;

/*
 *  source": {
	"id": "techcrunch",
	"name": "TechCrunch"
	},
	"author": "Romain Dillet",
	"title": "Coinbase users can now withdraw Bitcoin SV following BCH fork",
	"description": "If you’re a Coinbase user, you may have seen some new tokens on your account. The Bitcoin Cash chain split into two different chains back in November. It means that if you held Bitcoin Cash on November 15, you became the lucky owner of Bitcoin SV and Bitcoin …",
	"url": "http://techcrunch.com/2019/02/15/coinbase-users-can-now-withdraw-bitcoin-sv-following-bch-fork/",
	"urlToImage": "https://techcrunch.com/wp-content/uploads/2017/08/bitcoin-split-2017a.jpg?w=711",
	"publishedAt": "2019-02-15T14:53:40Z",
	"content": "If youre a Coinbase user, you may have seen some new tokens on your account. The Bitcoin Cash chain split into two different chains back in November. It means that if you held Bitcoin Cash on November 15, you became the lucky owner of Bitcoin SV and Bitcoin A… [+1514 chars]"
 */

public class Article extends SentimentAnalyzable
{
    private String author;
    private String title;
    private String description;
    private String content;
    private LocalDateTime publishedAt;
    private String url;
    private String urlToImage;
    private ArticleSource source;
    private String summary;
    private Image image;
    private ArticleFeed articleFeed;

    public Article(JSONObject jo)
    {
        this.author = jo.optString("author", "N/A");
        this.title = jo.optString("title", "N/A");
        this.description = jo.optString("description", "N/A");
        String tmp = jo.optString("content", "N/A");
        this.content = tmp.replaceAll("\\[\\+\\d+ chars\\]", "");
        this.summary = description;

        this.url = jo.optString("url", "http://koltunm.com/");
        this.urlToImage = jo.optString("urlToImage", "http://koltunm.com/");

        String sPublishedAt = jo.optString("publishedAt", "N/A");
        Instant instant = Instant.parse(sPublishedAt);
        this.publishedAt = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));

        String nameSource = jo.optString("name", "N/A");
        String idSource = jo.optString("id", nameSource);
        this.source = new ArticleSource(idSource, nameSource);
        this.image = null;
    }

    public String toString()
    {
        return String.format("%s\n%s\n\t%s\n",
                this.getPublishedAt().toString(),
                this.getTitle(),
                this.getDescription());
    }

    public void loadImage()
    {
        if (image == null)
        {
            image = new Image(urlToImage);
            Log.i(String.format("IMAGE LOADED %s", title));
        }
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getContent() {
        return content;
    }
    public String getAuthor() {
        return author;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrlToImage() {
        return urlToImage;
    }
    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
    public ArticleSource getSource() {
        return source;
    }
    public String getSummary()
    {
        return summary;
    }
    public Image getImage() { return image; }
    public ArticleFeed getArticleFeed()
    {
        if (articleFeed == null)
        {
            setArticleFeed(new ArticleFeed(title, summary, this, image));
        }
        return articleFeed;
    }
    public void setArticleFeed(ArticleFeed articleFeed) { this.articleFeed = articleFeed; }

    @Override
    public String sentimentString()
    {
        return String.format("%s.\n%s.", getTitle(), getSummary());
    }

    @Override
    public String getSentimentHeading()
    {
        return String.format("ARTICLE: %s", getTitle());
    }

    @Override
    public LocalDateTime getDateTime()
    {
        return getPublishedAt();
    }
}
