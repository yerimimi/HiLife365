package com.hilifecare.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-02-08.
 */

public class Program implements Serializable {
    private String schdule_id;
    private int day;
    private String explain;
    private String image;
    private int week;
    private String type;
    private String step;
    private String level;
    private String exercise_name;
    private String simple_explain;
    private int play_tiem;

    public Program() {
    }

    public Program(String schdule_id, int day, String explain, String image, int week, String type, String step, String level, String exercise_name, String simple_explain, int play_tiem) {
        this.schdule_id = schdule_id;
        this.day = day;
        this.explain = explain;
        this.image = image;
        this.week = week;
        this.type = type;
        this.step = step;
        this.level = level;
        this.exercise_name = exercise_name;
        this.simple_explain = simple_explain;
        this.play_tiem = play_tiem;
    }

    public String getSchdule_id() {
        return schdule_id;
    }

    public void setSchdule_id(String schdule_id) {
        this.schdule_id = schdule_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

    public String getSimple_explain() {
        return simple_explain;
    }

    public void setSimple_explain(String simple_explain) {
        this.simple_explain = simple_explain;
    }

    public int getPlay_tiem() {
        return play_tiem;
    }

    public void setPlay_tiem(int play_tiem) {
        this.play_tiem = play_tiem;
    }
}
