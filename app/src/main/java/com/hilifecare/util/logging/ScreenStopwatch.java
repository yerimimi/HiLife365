package com.hilifecare.util.logging;

import android.os.SystemClock;
import android.util.Log;

public class ScreenStopwatch {

    public static String TAG = "ElapsedTime";

    private long startMillis;

    public void reset() {
        startMillis = SystemClock.elapsedRealtime();
    }

    public long getElapsedTime() {
        long stopMillis = SystemClock.elapsedRealtime();
        long elapsedMillis = stopMillis - startMillis;
        return elapsedMillis;
    }

    public String getElapsedTimeString() {
        double seconds = (double)getElapsedTime() / 1000.0;
        if (seconds < 1.0) {
            return String.format("%.0f ms", seconds * 1000);
        }
        else {
            return String.format("%.2f s", seconds);
        }
    }

    public void printElapsedTimeLog(String title){
        Log.i(TAG, title+" elapsed time: " + getElapsedTimeString());
    }

    @Override
    public String toString() {
        return TAG + ": " + getElapsedTimeString();
    }

    private static ScreenStopwatch singleton = new ScreenStopwatch();

    private ScreenStopwatch() {
    }

    public static ScreenStopwatch getInstance() {
        return singleton;
    }

}