package com.parrot.audric.parrotzik.zikapi;

import android.os.Parcel;
import android.os.Parcelable;

public class State implements Parcelable {



    public static class Battery implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.level);
            dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        }

        public Battery() {
        }

        protected Battery(Parcel in) {
            this.level = in.readInt();
            int tmpState = in.readInt();
            this.state = tmpState == -1 ? null : BatteryState.values()[tmpState];
        }

        public static final Creator<Battery> CREATOR = new Creator<Battery>() {
            @Override
            public Battery createFromParcel(Parcel source) {
                return new Battery(source);
            }

            @Override
            public Battery[] newArray(int size) {
                return new Battery[size];
            }
        };
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.battery, flags);
        dest.writeByte(this.noiseCancellation ? (byte) 1 : (byte) 0);
        dest.writeByte(this.equalizer ? (byte) 1 : (byte) 0);
        dest.writeByte(this.concertHall ? (byte) 1 : (byte) 0);
    }

    public State() {
    }

    protected State(Parcel in) {
        this.battery = in.readParcelable(Battery.class.getClassLoader());
        this.noiseCancellation = in.readByte() != 0;
        this.equalizer = in.readByte() != 0;
        this.concertHall = in.readByte() != 0;
    }

    public static final Creator<State> CREATOR = new Creator<State>() {
        @Override
        public State createFromParcel(Parcel source) {
            return new State(source);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };
}

