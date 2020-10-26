package com.koltunm.skanner.util;

import java.io.IOException;
import java.util.logging.*;

public class Log
{
    private static Logger logger = null;

    private Log() {}

    public static Logger init(String loggerName, String logPath) throws IOException
    {
        if (Log.logger == null)
            Log.logger = setupLogger(loggerName, logPath);
        return Log.logger;
    }

    protected static Logger getLogger()
    {
        isInit();
        return Log.logger;
    }

    private static Logger setupLogger(String loggerName, String logPath) throws IOException
    {
        FileHandler fileHandler = new FileHandler(logPath);
        ConsoleHandler consHandler = new ConsoleHandler();

        Formatter lf = new LogFormatter();

        fileHandler.setFormatter(lf);
        consHandler.setFormatter(lf);

        Logger log = Logger.getLogger(loggerName);
        log.setUseParentHandlers(false);
        log.addHandler(fileHandler);
        log.addHandler(consHandler);
        log.setLevel(Level.FINE);
        log.setUseParentHandlers(false);
        return log;
    }

    private static void isInit()
    {
        if (Log.logger == null)
            throw new RuntimeException("Logger must be first initialized!");
    }

    public static void i(Object object)
    {
        isInit();
        Log.logger.info(object.toString());
    }

    public static void d(Object object)
    {
        isInit();
        Log.logger.fine(object.toString());
    }

    public static void e(Object object, Exception e)
    {
        isInit();
        Log.logger.throwing(object.toString(), e.getMessage(), e);
    }

    public static void w(Object object)
    {
        isInit();
        Log.logger.warning(object.toString());
    }
}
