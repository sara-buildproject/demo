package com.buildsessions.stockmarketanalyzer.model.financialmodeling;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FmpStockSearchResponse {
    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("name")
    private String name;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
