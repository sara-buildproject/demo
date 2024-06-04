package com.buildsessions.stockmarketanalyzer.repository;

import com.buildsessions.stockmarketanalyzer.entity.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {
    List<StockHistory> findBySymbolAndDateBetween(String symbol, Date startDate, Date endDate);

    void deleteBySymbol(String symbol);
}
