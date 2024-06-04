package com.buildsessions.stockmarketanalyzer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class StockHistory {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id private Long id;
    private double close;
    private Date date;
    private String symbol;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
