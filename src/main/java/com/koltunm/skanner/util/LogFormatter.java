package com.koltunm.skanner.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter
{
    private final DateFormat df
            = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    public String format(LogRecord r)
    {
        String pkgClass = r.getSourceClassName() + "." + r.getSourceMethodName();
        String date = df.format(new Date(r.getMillis()));
        String threadName = Thread.currentThread().getName();
        String msg = formatMessage(r);
        String lvl = r.getLevel().toString();
        return String.format("%s%10s%35s%30s : %s\n", date, lvl, pkgClass, threadName, msg);
    }
}
