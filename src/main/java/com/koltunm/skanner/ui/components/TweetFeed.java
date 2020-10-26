package com.koltunm.skanner.ui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDateTime;

public class TweetFeed extends AnchorPane
{
    private final String FXML_FILE = "../../../../../fxmls/tweet_feed.fxml";

    @FXML public Label lblDate;
    @FXML public Label lblUser;
    @FXML public Label lblTweetMessage;

    public TweetFeed(LocalDateTime date, String user, String message)
    {
        super();
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
        setUser(user);
        setMessage(message);
        setDate(date);
    }

    private void setDate(LocalDateTime date)
    {
        lblDate.setText(date.toString());
    }

    private void setMessage(String message)
    {
        lblTweetMessage.setText(message);
    }

    private void setUser(String user)
    {
        lblUser.setText(String.format("@%s", user));
    }
}
