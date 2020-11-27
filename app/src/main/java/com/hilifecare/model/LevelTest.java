package com.hilifecare.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-02-08.
 */

public class LevelTest implements Serializable{
    private String stage;
    private int step;
    private String name;
    private String descriptions;
    private int measure_secs;
    private ArrayList<Integer> guideline = new ArrayList<>();
    private String image;
    private String repeat_video;

    public LevelTest() {

    }

    public LevelTest(String name, String descriptions, int measure_secs, ArrayList<Integer> guideline, String image, String repeat_video) {
        this.name = name;
        this.descriptions = descriptions;
        this.measure_secs = measure_secs;
        this.guideline = guideline;
        this.image = image;
        this.repeat_video = repeat_video;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public int getMeasure_secs() {
        return measure_secs;
    }

    public void setMeasure_secs(int measure_secs) {
        this.measure_secs = measure_secs;
    }

    public ArrayList<Integer> getGuideline() {
        return guideline;
    }

    public void setGuideline(ArrayList<Integer> guideline) {
        this.guideline = guideline;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRepeat_video() {
        return repeat_video;
    }

    public void setRepeat_video(String repeat_video) {
        this.repeat_video = repeat_video;
    }
}
