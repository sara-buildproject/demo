package com.buildsessions.stockmarketanalyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AlphaVantageSearchResponse {

    @JsonProperty("bestMatches")
    private List<AlphaVantageSymbolMatch> bestMatches;

    public List<AlphaVantageSymbolMatch> getBestMatches() {
        return bestMatches;
    }

    public void setBestMatches(List<AlphaVantageSymbolMatch> bestMatches) {
        this.bestMatches = bestMatches;
    }
}

