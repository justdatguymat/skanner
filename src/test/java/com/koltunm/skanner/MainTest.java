package com.koltunm.skanner;

import com.koltunm.skanner.clients.finance.YahooClient;
import com.koltunm.skanner.clients.news.NewsClient;
import com.koltunm.skanner.ui.controllers.MenuWindow;
import com.koltunm.skanner.util.Config;
import com.koltunm.skanner.util.Log;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class MainTest extends ApplicationTest
{
    private NewsClient newsClient;
    private Skanner skanner;
    private Properties config;
    private YahooClient yahooClient;
    private MenuWindow menuWindow;
    private static final String defaultConfigFile = "skanner.properties";


    private void initModules()
    {
        try
        {
            config = Config.init(defaultConfigFile);
            skanner = Skanner.init();
            Log.init(skanner.getClass().getPackage().getName(), Config.getProperty("logs.path"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testOpenFetcherWindow()
    {
        clickOn(menuWindow.btnFetch);
    }

    @Before
    public void setUp() throws Exception
    {
        initModules();
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}