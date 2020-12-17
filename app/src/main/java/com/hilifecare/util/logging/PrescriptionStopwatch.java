package com.hilifecare.util.logging;

/**
 * Created by SETUP on 2020-11-28.
 */

public class PrescriptionStopwatch extends AbstractStopwatch {

    private static PrescriptionStopwatch singleton = new PrescriptionStopwatch();

    protected PrescriptionStopwatch() {
    }

    public static PrescriptionStopwatch getInstance() {
        return singleton;
    }

    protected String getTag() {
        return "ET_1 ";
    }

}