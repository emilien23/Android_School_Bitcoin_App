package com.androidschool_bitcoin_app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Эмиль on 23.08.2017.
 */

public class BitcoinHistory {

    @SerializedName("Data")
    List<Data> data;

    @SerializedName("TimeTo")
    long timeTo;

    @SerializedName("TimeFrom")
    long timeFrom;

    @SerializedName("Response")
    String response;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public long getTimeTo() {
        return timeTo;
    }

    public long getTimeFrom() {
        return timeFrom;
    }

    public void setTimeTo(long timeTo) {
        this.timeTo = timeTo;
    }

    public void setTimeFrom(long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
