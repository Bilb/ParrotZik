package com.parrot.audric.parrotzik.zikapi;

public class State {



    public static class Battery {
        public enum BatteryState { CHARGING, CHARGED, IN_USE, UNKNOW};
        public int level;
        public BatteryState state = BatteryState.UNKNOW;

        @Override
        public String toString() {
            return "Battery{" +
                    "level=" + level +
                    ", state=" + state +
                    '}';
        }
    }

    private Battery battery = new Battery();

    private boolean noiseCancellation, equalizer, concertHall;


    public Battery getBattery() {
        return battery;
    }


    public boolean getNoiseCancellation() {
        return noiseCancellation;
    }

    public void setNoiseCancellation(boolean noiseCancellation) {
        this.noiseCancellation = noiseCancellation;
    }

    public boolean getEqualizer() {
        return equalizer;
    }

    public void setEqualizer(boolean equalizer) {
        this.equalizer = equalizer;
    }

    public boolean getConcertHall() {
        return concertHall;
    }

    public void setConcertHall(boolean concertHall) {
        this.concertHall = concertHall;
    }


    @Override
    public String toString() {
        return "State{" +
                "battery=" + battery +
                ", noiseCancellation=" + noiseCancellation +
                ", equalizer=" + equalizer +
                ", concertHall=" + concertHall +
                '}';
    }
}

