package com.androidschool_bitcoin_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Эмиль on 23.08.2017.
 */

public class Bitcoin implements Parcelable {

    String id;

    String name;

    String symbol;

    @SerializedName("price_usd")
    String priceUsd;

    @SerializedName("percent_change_1h")
    String percentChange1h;

    @SerializedName("percent_change_24h")
    String percentChange24h;

    @SerializedName("percent_change_7d")
    String percentChange7d;

    @SerializedName("last_updated")
    String lastUpdated;

    String image;

    @SerializedName("price_eur")
    String priceEur;

    @SerializedName("price_rub")
    String priceRub;

    public Bitcoin(Parcel in) {
        String[] data = new String[10];
        in.readStringArray(data);
        id = data[0];
        name = data[1];
        symbol = data[2];
        priceUsd = data[3];
        percentChange1h = data[4];
        percentChange24h = data[5];
        percentChange7d = data[6];
        lastUpdated = data[7];
        priceEur = data[8];
        priceRub = data[9];
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getPercentChange1h() {
        return percentChange1h;
    }

    public String getPercentChange7d() {
        return percentChange7d;
    }

    public String getPercentChange24h() {
        return percentChange24h;
    }

    public String getPriceUsd() {
        return priceUsd;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPercentChange1h(String percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    public void setPercentChange7d(String percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    public void setPercentChange24h(String percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPriceEur() {
        return priceEur;
    }

    public String getPriceRub() {
        return priceRub;
    }

    public void setPriceEur(String priceEur) {
        this.priceEur = priceEur;
    }

    public void setPriceRub(String priceRub) {
        this.priceRub = priceRub;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {  id,  name,  symbol,  priceUsd,
                 percentChange1h,  percentChange24h,  percentChange7d,  lastUpdated, priceEur, priceRub });
    }

    public static final Parcelable.Creator<Bitcoin> CREATOR = new Parcelable.Creator<Bitcoin>() {

        @Override
        public Bitcoin createFromParcel(Parcel source) {
            return new Bitcoin(source);
        }

        @Override
        public Bitcoin[] newArray(int size) {
            return new Bitcoin[size];
        }
    };

}
