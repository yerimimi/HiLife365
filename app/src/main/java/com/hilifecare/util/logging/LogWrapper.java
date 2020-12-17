package com.hilifecare.util.logging;

import android.os.Binder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogWrapper {
    private static final String LOG_FILE_NAME = "HyoilFileLog_%g.txt";
    private static Logger logger;
    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler(Environment.getExternalStorageDirectory() + File.separator + LOG_FILE_NAME, true);
            fileHandler.setFormatter(new java.util.logging.Formatter() {
                @Override
                public String format(LogRecord r) {
                    StringBuilder ret = new StringBuilder(80);
                    ret.append(r.getMessage());
                    return ret.toString();
                }
            });
            logger = Logger.getLogger(LogWrapper.class.getName());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
        }
    }

    public static void v(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d): %s\n", tag, Binder.getCallingPid(), msg));
        }
        Log.v(tag, msg);
    }
}
