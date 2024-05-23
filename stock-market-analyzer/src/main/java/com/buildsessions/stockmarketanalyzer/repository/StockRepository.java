package com.buildsessions.stockmarketanalyzer.repository;

import com.buildsessions.stockmarketanalyzer.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock findBySymbol(String symbol);
}
