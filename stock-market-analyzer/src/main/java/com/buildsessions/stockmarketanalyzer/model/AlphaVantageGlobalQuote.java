package com.buildsessions.stockmarketanalyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlphaVantageGlobalQuote {

    @JsonProperty("01. symbol")
    private String symbol;

    @JsonProperty("05. price")
    private String currentPrice;

    @JsonProperty("09. change")
    private String priceChange;

    @JsonProperty("10. change percent")
    private String percentageChange;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(String percentageChange) {
        this.percentageChange = percentageChange;
    }
}
