package com.buildsessions.stockmarketanalyzer.repository;

import com.buildsessions.stockmarketanalyzer.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock findBySymbol(String symbol);
    Optional<Stock> findById(Long id);
}
