package com.koltunm.skanner;

import java.io.IOException;
import com.koltunm.skanner.ui.controllers.MenuWindow;
import com.koltunm.skanner.util.Config;
import com.koltunm.skanner.util.Log;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    private static final String defaultConfigFile = "skanner.properties";

    public void start(Stage stage)
    {
        MenuWindow mainMenu = new MenuWindow();
        stage.setTitle(Config.getProperty("window.title"));
        stage.setScene(new Scene(mainMenu));
        stage.show();
    }

    private static void initModules() throws IOException
    {
        Config.init(defaultConfigFile);
        Skanner skanner = Skanner.init();
        Log.init(skanner.getClass().getPackage().getName(), Config.getProperty("logs.path"));
    }

    public static void main(String[] args) throws Exception
    {
        initModules();
        Log.i("\n\n****** LAUNCHING THE APPLICATION ******\n\n");
        launch(args);
        Log.i("\n\n****** APPLICATION HAS BEEN CLOSED ******\n\n");

        /*
        //DataSet dataSet = new DataSet(Config.getProperty("app.datasetFileName"));
        ConverterUtils.DataSource source1 = new ConverterUtils.DataSource("test.arff");
        Instances dataSet = source1.getDataSet();

        Log.i(dataSet);

        //dataSet.removeAttributes();
        //Log.i(dataSet);

        Log.i(LinearRegressionModel.predict(dataSet));
        double priceChange = LinearRegressionModel.predict(dataSet);
        double actual = 0.429641;
        Log.i(String.format("%n ****** %ACTUAL PRICE: %d%nPREDICTED: %d", actual, priceChange));
         */
    }
}
