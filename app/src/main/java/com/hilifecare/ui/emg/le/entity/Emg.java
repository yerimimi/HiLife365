package com.hilifecare.ui.emg.le.entity;

import java.io.Serializable;

public class Emg implements Serializable {
    int deviceId;
    int w;
    int x;
    int y;
    int z;
    int emg;

    public Emg(int deviceId, int w, int x, int y, int z, int emg) {
        this.deviceId = deviceId;
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
        this.emg = emg;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getEmg() {
        return emg;
    }

    public void setEmg(int emg) {
        this.emg = emg;
    }

    @Override
    public String toString() {
        return "Emg{" +
                "deviceId=" + deviceId +
                ", w=" + w +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", emg=" + emg +
                '}';
    }
}
