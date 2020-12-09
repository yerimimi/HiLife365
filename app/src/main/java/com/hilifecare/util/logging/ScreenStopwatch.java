package com.hilifecare.util.logging;

import android.os.SystemClock;
import android.util.Log;

public class ScreenStopwatch extends AbstractStopwatch {

    private static ScreenStopwatch singleton = new ScreenStopwatch();

    protected ScreenStopwatch() {
    }

    public static ScreenStopwatch getInstance() {
        return singleton;
    }

    protected String getTag() {
        return "ElapsedTime_2";
    }

}