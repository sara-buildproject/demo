package com.buildsessions.stockmarketanalyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlphaVantageGlobalQuoteResponse {

    @JsonProperty("Global Quote")
    private AlphaVantageGlobalQuote globalQuote;

    public AlphaVantageGlobalQuote getGlobalQuote() {
        return globalQuote;
    }

    public void setGlobalQuote(AlphaVantageGlobalQuote globalQuote) {
        this.globalQuote = globalQuote;
    }
}

