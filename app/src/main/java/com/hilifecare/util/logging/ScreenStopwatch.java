package com.hilifecare.util.logging;

public class ScreenStopwatch extends AbstractStopwatch {

    private static ScreenStopwatch singleton = new ScreenStopwatch();

    protected ScreenStopwatch() {
    }

    public static ScreenStopwatch getInstance() {
        return singleton;
    }

    protected String getTag() {
        return "ET_2 ";
    }

}