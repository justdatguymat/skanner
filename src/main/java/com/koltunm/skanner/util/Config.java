package com.koltunm.skanner.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config
{
    private static Properties properties = null;

    public static Properties init(String configFile) throws IOException
    {
        if (Config.properties == null)
            properties = loadConfig(configFile);
        return Config.properties;
    }

    public static Properties getProperties() throws RuntimeException
    {
        isInit();
        return Config.properties;
    }

    private static void isInit()
    {
        if (Config.properties == null)
            throw new RuntimeException("Config must be first initialized!");
    }

    private static Properties loadConfig(String configString) throws IOException
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = loader.getResourceAsStream(configString);
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    public static String getProperty(String key)
    {
        isInit();
        return Config.properties.getProperty(key);
    }
}
