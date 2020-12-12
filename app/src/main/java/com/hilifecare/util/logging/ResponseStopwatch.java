package com.hilifecare.util.logging;

/**
 * Created by SETUP on 2020-11-28.
 */

public class ResponseStopwatch extends AbstractStopwatch {

    private static ResponseStopwatch singleton = new ResponseStopwatch();

    protected ResponseStopwatch() {
    }

    public static ResponseStopwatch getInstance() {
        return singleton;
    }

    protected String getTag() {
        return "ElapsedTime_5";
    }

}