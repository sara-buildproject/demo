package com.buildsessions.stockmarketanalyzer.model.financialmodeling;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FmpHistoricalData {

    @JsonProperty("date")
    private String date;

    @JsonProperty("close")
    private double close;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }
}
