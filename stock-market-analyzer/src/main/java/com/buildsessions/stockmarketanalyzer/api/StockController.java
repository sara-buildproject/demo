package com.buildsessions.stockmarketanalyzer.api;


import com.buildsessions.stockmarketanalyzer.entity.StockHistory;
import com.buildsessions.stockmarketanalyzer.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{symbol}/history")
    public ResponseEntity<List<StockHistory>> getStockHistory(
            @PathVariable String symbol,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<StockHistory> historicalData = stockService.getHistoricalStockData(symbol, start, end);
        if (historicalData != null && !historicalData.isEmpty()) {
            return new ResponseEntity<>(historicalData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
