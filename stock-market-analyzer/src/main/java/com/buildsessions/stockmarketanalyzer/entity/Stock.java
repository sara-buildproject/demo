package com.buildsessions.stockmarketanalyzer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Stock {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String symbol;
    private String name;
    private double currentPrice;
    private double priceChange;
    private double percentageChange;

    public Stock(Long id, String symbol, String name, double currentPrice, double priceChange, double percentageChange) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.priceChange = priceChange;
        this.percentageChange = percentageChange;
    }

    public Stock() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock stock)) return false;
        return Double.compare(currentPrice, stock.currentPrice) == 0 && Double.compare(priceChange, stock.priceChange) == 0 && Double.compare(percentageChange, stock.percentageChange) == 0 && Objects.equals(id, stock.id) && Objects.equals(symbol, stock.symbol) && Objects.equals(name, stock.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbol, name, currentPrice, priceChange, percentageChange);
    }
}
