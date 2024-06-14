package com.buildsessions.stockmarketanalyzer.model.financialmodeling;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FmpHistoricalResponse {

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("historical")
    private List<FmpHistoricalData> historicalData;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<FmpHistoricalData> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(List<FmpHistoricalData> historicalData) {
        this.historicalData = historicalData;
    }
}

