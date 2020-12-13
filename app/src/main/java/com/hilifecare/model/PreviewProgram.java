package com.hilifecare.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-02-08.
 */

public class PreviewProgram implements Serializable {
    private String user_id;
    private String schdule_name;
    private String exercise_name;
    private int day;
    private int step;
    private String type;

    public PreviewProgram() {
    }

    public PreviewProgram(String user_id, String schdule_name, String exercise_name, int day, int step, String type) {
        this.user_id = user_id;
        this.schdule_name = schdule_name;
        this.exercise_name = exercise_name;
        this.day = day;
        this.step = step;
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSchdule_name() {
        return schdule_name;
    }

    public void setSchdule_name(String schdule_name) {
        this.schdule_name = schdule_name;
    }

    public String getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
