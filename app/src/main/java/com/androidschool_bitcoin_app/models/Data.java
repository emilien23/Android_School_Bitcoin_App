package com.androidschool_bitcoin_app.models;

/**
 * Created by Эмиль on 23.08.2017.
 */

public class Data {
    long time;

    Float close;

    Float high;

    Float open;

    Float low;

    public Float getClose() {
        return close;
    }

    public Float getHigh() {
        return high;
    }

    public long getTime() {
        return time;
    }

    public void setClose(Float close) {
        this.close = close;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Float getLow() {
        return low;
    }

    public Float getOpen() {
        return open;
    }

    public void setHigh(Float high) {
        this.high = high;
    }

    public void setLow(Float low) {
        this.low = low;
    }

    public void setOpen(Float open) {
        this.open = open;
    }
}
