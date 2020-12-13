package com.hilifecare.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-02-08.
 */

public class Exercise implements Serializable{
    private String id;
    private String name;
    private String level;
    private String simple_explain;
    private String category;
    private String category_value;
    private String image;
    private String video;
    private String repeat_video;
    private ArrayList<String> place = new ArrayList<>();

    public Exercise() {

    }

    public Exercise(String name, String level, String category, String image) {
        this.name = name;
        this.level = level;
        this.category = category;
        this.image = image;
    }

    public Exercise(String id, String name, String level, String simple_explain, String category, String category_value, String image, String video, String repeat_video, ArrayList<String> place) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.simple_explain = simple_explain;
        this.category = category;
        this.category_value = category_value;
        this.image = image;
        this.video = video;
        this.repeat_video = repeat_video;
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSimple_explain() {
        return simple_explain;
    }

    public void setSimple_explain(String simple_explain) {
        this.simple_explain = simple_explain;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_value() {
        return category_value;
    }

    public void setCategory_value(String category_value) {
        this.category_value = category_value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getRepeat_video() {
        return repeat_video;
    }

    public void setRepeat_video(String repeat_video) {
        this.repeat_video = repeat_video;
    }

    public ArrayList<String> getPlace() {
        return place;
    }

    public void setPlace(ArrayList<String> place) {
        this.place = place;
    }
}
