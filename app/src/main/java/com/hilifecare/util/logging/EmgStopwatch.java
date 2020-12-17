package com.hilifecare.util.logging;

/**
 * Created by SETUP on 2020-11-28.
 */

public class EmgStopwatch extends AbstractStopwatch {

    private static EmgStopwatch singleton = new EmgStopwatch();

    protected EmgStopwatch() {
    }

    public static EmgStopwatch getInstance() {
        return singleton;
    }

    protected String getTag() {
        return "ET_4_EMG ";
    }

}