package com.hilifecare.util.logging;

/**
 * Created by SETUP on 2020-11-28.
 */

public class HrStopwatch extends AbstractStopwatch {

    private static HrStopwatch singleton = new HrStopwatch();

    protected HrStopwatch() {
    }

    public static HrStopwatch getInstance() {
        return singleton;
    }

    protected String getTag() {
        return "ET_4_HR ";
    }

}