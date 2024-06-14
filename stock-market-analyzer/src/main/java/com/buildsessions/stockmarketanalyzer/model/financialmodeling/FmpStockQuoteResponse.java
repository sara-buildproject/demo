package com.buildsessions.stockmarketanalyzer.model.financialmodeling;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FmpStockQuoteResponse {
    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("price")
    private double price;

    @JsonProperty("change")
    private double change;

    @JsonProperty("changesPercentage")
    private double changesPercentage;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangesPercentage() {
        return changesPercentage;
    }

    public void setChangesPercentage(double changesPercentage) {
        this.changesPercentage = changesPercentage;
    }
}

