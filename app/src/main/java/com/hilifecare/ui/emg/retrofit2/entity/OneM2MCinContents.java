package com.hilifecare.ui.emg.retrofit2.entity;

import com.google.gson.annotations.SerializedName;

public class OneM2MCinContents {
    @SerializedName("cnf")
    private String cnf;
    @SerializedName("con")
    private String con;

    public OneM2MCinContents(String cnf, String con) {
        this.cnf = cnf;
        this.con = con;
    }

    public String getCnf() {
        return cnf;
    }

    public void setCnf(String cnf) {
        this.cnf = cnf;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    @Override
    public String toString() {
        return "OneM2MCinContents{" +
                "cnf='" + cnf + '\'' +
                ", con=" + con +
                '}';
    }
}

