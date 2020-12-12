package com.hilifecare.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-02-08.
 */

public class ReformProgram implements Serializable {
    private int day;
    private String explain;
    private String image;
    private int week;
    private int play_tiem;
    private int exercise_count;

    public ReformProgram() {
    }

    public ReformProgram(int day, String explain, String image, int week, int play_tiem, int exercise_count) {
        this.day = day;
        this.explain = explain;
        this.image = image;
        this.week = week;
        this.play_tiem = play_tiem;
        this.exercise_count = exercise_count;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getPlay_tiem() {
        return play_tiem;
    }

    public void setPlay_tiem(int play_tiem) {
        this.play_tiem = play_tiem;
    }

    public int getExercise_count() {
        return exercise_count;
    }

    public void setExercise_count(int exercise_count) {
        this.exercise_count = exercise_count;
    }
}
