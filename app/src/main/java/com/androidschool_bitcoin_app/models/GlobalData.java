package com.androidschool_bitcoin_app.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Эмиль on 23.08.2017.
 */

public class GlobalData {
    @SerializedName("total_market_cap_usd")
    long totalMarketCapUsd;
    @SerializedName("bitcoin_percentage_of_market_cap")
    Float bitcoinPercentage;
    @SerializedName("active_currencies")
    Integer activeCurrencies;
    @SerializedName("active_markets")
    Integer activeMarkets;

    @SerializedName("total_market_cap_eur")
    long totalMarketCapEur;

    @SerializedName("total_market_cap_rub")
    long totalMarketCapRub;


    String currency;

    public Float getBitcoinPercentage() {
        return bitcoinPercentage;
    }

    public long getTotalMarketCapUsd() {
        return totalMarketCapUsd;
    }

    public void setTotalMarketCapUsd(long totalMarketCapUsd) {
        this.totalMarketCapUsd = totalMarketCapUsd;
    }

    public Integer getActiveCurrencies() {
        return activeCurrencies;
    }

    public void setBitcoinPercentage(Float bitcoinPercentage) {
        this.bitcoinPercentage = bitcoinPercentage;
    }

    public Integer getActiveMarkets() {
        return activeMarkets;
    }

    public void setActiveCurrencies(Integer activeCurrencies) {
        this.activeCurrencies = activeCurrencies;
    }

    public void setActiveMarkets(Integer activeMarkets) {
        this.activeMarkets = activeMarkets;
    }

    public long getTotalMarketCapEur() {
        return totalMarketCapEur;
    }

    public long getTotalMarketCapRub() {
        return totalMarketCapRub;
    }

    public void setTotalMarketCapEur(long totalMarketCapEur) {
        this.totalMarketCapEur = totalMarketCapEur;
    }

    public void setTotalMarketCapRub(long totalMarketCapRub) {
        this.totalMarketCapRub = totalMarketCapRub;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
