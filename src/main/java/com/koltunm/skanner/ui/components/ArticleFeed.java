package com.koltunm.skanner.ui.components;

import com.koltunm.skanner.clients.news.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ArticleFeed extends GridPane
{
    private final String FXML_FILE = "../../../../../fxmls/article_feed.fxml";

    @FXML public Label lblArticleTitle;
    @FXML public Label lblArticleSummary;
    @FXML public Hyperlink linkArticleTitle;
    @FXML public ImageView imgArticle;

    final private Article article;
    private boolean flag = true;
    private Image image;

    public ArticleFeed(String title, String summary, Article article, Image image)
    {
        super();
        this.article = article;
        this.image = image;
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_FILE));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        }
        catch (IOException exception)
        {
            throw new RuntimeException(exception);
        }
        lblArticleTitle.setText(title);
        lblArticleSummary.setText(summary);
        setImage(this.image);
    }

    public void openArticleLink(ActionEvent actionEvent)
    {
        flag = !flag;
        lblArticleTitle.getParent().setVisible(flag);
    }

    public void setImage(Image image)
    {
        this.image = image;
        imgArticle.setImage(this.image);
    }

    public Article getArticle()
    {
        return this.article;
    }
}
