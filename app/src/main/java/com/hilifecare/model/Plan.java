package com.hilifecare.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-02-08.
 */

public class Plan implements Serializable {
    private String plan_id;
    private String grade;
    private String name;
    private String image;
    private String explain;
    private String disclosure;

    public Plan() {
    }

    public Plan(String plan_id, String grade, String name, String image, String explain, String disclosure) {
        this.plan_id = plan_id;
        this.grade = grade;
        this.name = name;
        this.image = image;
        this.explain = explain;
        this.disclosure = disclosure;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getDisclosure() {
        return disclosure;
    }

    public void setDisclosure(String disclosure) {
        this.disclosure = disclosure;
    }
}
