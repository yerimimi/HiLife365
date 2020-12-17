package com.hilifecare.util.logging;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogWrapperOld {
    private static final String TAG = "LogWrapper";
    private static final int LOG_FILE_SIZE_LIMIT = 1024 * 1024 * 10;
    private static final int LOG_FILE_MAX_COUNT = 100;
    private static final String LOG_FILE_NAME = "HyoilFileLog_%g.txt";
    private static final SimpleDateFormat formatter =
            new SimpleDateFormat("MMddHHmmss: ", Locale.getDefault());
    private static final Date date = new Date();
    private static Logger logger;
    private static FileHandler fileHandler;

    static {
        try {
            Log.i("LogWrapper", "state: " + Environment.getExternalStorageState());
            Log.i("LogWrapper", "dir: " + Environment.getExternalStorageDirectory());
            Log.i("LogWrapper", "dir2: " + Environment.getExternalStorageDirectory() + File.separator + LOG_FILE_NAME);
            Log.i("LogWrapper", "state2: " + Environment.getExternalStorageState(new File(Environment.getExternalStorageDirectory() + File.separator + LOG_FILE_NAME)));

            fileHandler = new FileHandler(
                    Environment.getExternalStorageDirectory() + File.separator + LOG_FILE_NAME,
                    LOG_FILE_SIZE_LIMIT, LOG_FILE_MAX_COUNT, true);
            fileHandler.setFormatter(new java.util.logging.Formatter() {
                @Override
                public String format(LogRecord r) {
                    date.setTime(System.currentTimeMillis());

                    StringBuilder ret = new StringBuilder(80);
                    ret.append(formatter.format(date));
                    ret.append(r.getMessage());
                    return ret.toString();
                }
            });
            logger = Logger.getLogger(LogWrapperOld.class.getName());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
            Log.d(TAG, "init success");
        } catch (IOException e) {
            Log.d(TAG, "init failure");
            e.printStackTrace();
            StackTraceElement[] stack = e.getStackTrace();
            for (StackTraceElement elem : stack) {
                Log.i("STACK", "" + elem.toString());
            }
            Log.i("LogWrapper", "err: " + e.getMessage());
        }
    }

    public static void v(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s: %s\n", tag, msg));
            //logger.log(Level.INFO, String.format("V/%s(%d): %s\n", tag, Binder.getCallingPid(), msg));
        }
        Log.v(tag, msg);
    }
}
