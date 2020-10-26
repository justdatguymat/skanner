package com.koltunm.skanner.ui.controllers;

import com.koltunm.skanner.Skanner;
import com.koltunm.skanner.SkannerInterface;
import com.koltunm.skanner.util.Config;
import com.koltunm.skanner.util.Log;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class WindowPane extends AnchorPane implements Initializable
{
    private Modality modality;
    private Scene scene;
    SkannerInterface skanner;
    MenuWindow menuWindow;

    public abstract void reset();
    public abstract void initialize(URL location, ResourceBundle resources);

    public WindowPane()
    {
        super();
        skanner = Skanner.getInstance();
    }

    void init(MenuWindow menuWindow, String fxmlLife)
    {
        this.menuWindow = menuWindow;
        URL path = null;
        try
        {
            path = getClass().getResource(fxmlLife);
            FXMLLoader loader = new FXMLLoader(path);
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            scene = new Scene(this);
        }
        catch (IOException exception)
        {
            System.out.println(path + "\n\n");
            Log.e(path, exception);
            throw new RuntimeException(exception);
        }
    }

    public void setModality(Modality modality)
    {
        this.modality = modality;
    }

    public void open()
    {
        Stage stage = new Stage();
        stage.initModality(modality);
        stage.setTitle(Config.getProperty("window.title"));
        stage.setScene(scene);
        stage.show();
        stage.toFront();
        reset();
    }

    void sendAlert(Alert.AlertType type, String msg)
    {
        Alert alert = new Alert(type);
        //alert.setTitle("BOOOO");
        //alert.setHeaderText("Something went wrong.");
        alert.setContentText(msg);
        alert.show();
    }
}
