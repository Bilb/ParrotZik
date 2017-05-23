package com.parrot.audric.parrotzik.zikapi;

public class State {
    private int batteryLevel;
    private String batteryState;
    private boolean noiseCancellation;


    public int getBatteryLevel() {
        return batteryLevel;
    }

    public String getBatteryState() {
        return batteryState;
    }


    public boolean getNoiseCancellation() {
        return noiseCancellation;
    }


    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }


    public void setBatteryState(String batteryState) {
        this.batteryState = batteryState;
    }

    public void setNoiseCancellation(boolean noiseCancellation) {
        this.noiseCancellation = noiseCancellation;
    }

    @Override
    public String toString() {
        return "State{" +
                "batteryLevel=" + batteryLevel +
                ", batteryState='" + batteryState + '\'' +
                ", noiseCancellation=" + noiseCancellation +
                '}';
    }
}
