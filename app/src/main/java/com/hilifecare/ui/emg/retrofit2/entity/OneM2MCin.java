package com.hilifecare.ui.emg.retrofit2.entity;

import com.google.gson.annotations.SerializedName;

public class OneM2MCin {
    @SerializedName("m2m:cin")
    private OneM2MCinContents cin;

    public OneM2MCin(OneM2MCinContents cin) {
        this.cin = cin;
    }

    public OneM2MCinContents getCin() {
        return cin;
    }

    public void setCin(OneM2MCinContents cin) {
        this.cin = cin;
    }

    @Override
    public String toString() {
        return "OneM2MCin{" +
                "cin=" + cin +
                '}';
    }
}

