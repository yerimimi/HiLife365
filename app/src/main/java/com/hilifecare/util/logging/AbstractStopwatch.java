package com.hilifecare.util.logging;

import android.os.SystemClock;

public abstract class AbstractStopwatch {

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
        double seconds = (double) getElapsedTime() / 1000.0;
        if (seconds < 1.0) {
            return String.format("%.0f ms", seconds * 1000);
        } else {
            return String.format("%.2f s", seconds);
        }
    }

    public void printElapsedTimeLog(String title) {
        LogWrapperOld.v(getTag(), title + " ET : " + getElapsedTimeString());
    }

    @Override
    public String toString() {
        return getTag() + ": " + getElapsedTimeString();
    }

    abstract protected String getTag();

}