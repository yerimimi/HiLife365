package com.hilifecare.util.logging;

/**
 * Created by SETUP on 2020-11-28.
 */

public class VisualStopwatch extends AbstractStopwatch {

    private static VisualStopwatch singleton = new VisualStopwatch();

    protected VisualStopwatch() {
    }

    public static VisualStopwatch getInstance() {
        return singleton;
    }

    protected String getTag() {
        return "ET_3 ";
    }

}