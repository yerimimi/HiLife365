package com.hilifecare.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-06-20.
 */

public class UserInfo implements Serializable {
    private String name;
    private String age;
    private String gender;
    private int tall;
    private int weight;
    private int fat;
    private int muscle;
    private int grade;

    public UserInfo() {
    }

    public UserInfo(String name, String age, String gender, int tall, int weight, int fat, int muscle, int grade) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.tall = tall;
        this.weight = weight;
        this.fat = fat;
        this.muscle = muscle;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getTall() {
        return tall;
    }

    public void setTall(int tall) {
        this.tall = tall;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getMuscle() {
        return muscle;
    }

    public void setMuscle(int muscle) {
        this.muscle = muscle;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
