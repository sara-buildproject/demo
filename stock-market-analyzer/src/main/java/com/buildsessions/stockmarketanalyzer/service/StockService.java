package com.buildsessions.stockmarketanalyzer.service;

import com.buildsessions.stockmarketanalyzer.entity.Stock;
import com.buildsessions.stockmarketanalyzer.entity.StockHistory;
import com.buildsessions.stockmarketanalyzer.model.financialmodeling.FmpHistoricalData;
import com.buildsessions.stockmarketanalyzer.model.financialmodeling.FmpHistoricalResponse;
import com.buildsessions.stockmarketanalyzer.model.financialmodeling.FmpStockQuoteResponse;
import com.buildsessions.stockmarketanalyzer.model.financialmodeling.FmpStockSearchResponse;
import com.buildsessions.stockmarketanalyzer.repository.StockHistoryRepository;
import com.buildsessions.stockmarketanalyzer.repository.StockRepository;
import com.buildsessions.stockmarketanalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {


    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Value("${financial.modelingprep.api.key}")
    private String fmpApiKey;

    private final String FMP_BASE_URL = "https://financialmodelingprep.com/api/v3";

    public Stock getOrCreateStockBySymbol(String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            stock = fetchAndStoreStockInfo(symbol);
        }
        return stock;
    }

    public Stock getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    public Stock getStockById(Long stockId) {
        return stockRepository.findById(stockId).get();
    }

    public Stock fetchAndStoreStockInfo(String symbol) {
        String searchUrl = FMP_BASE_URL + "/search?query=" + symbol + "&apikey=" + fmpApiKey;
        RestTemplate restTemplate = new RestTemplate();
        FmpStockSearchResponse[] searchResponse = restTemplate.getForObject(searchUrl, FmpStockSearchResponse[].class);

        if (searchResponse != null && searchResponse.length > 0) {
            FmpStockSearchResponse symbolMatch = searchResponse[0];

            String retrievedSymbol = symbolMatch.getSymbol();
            String name = symbolMatch.getName();

            String quoteUrl = FMP_BASE_URL + "/quote/" + symbol + "?apikey=" + fmpApiKey;
            FmpStockQuoteResponse[] quoteResponse = restTemplate.getForObject(quoteUrl, FmpStockQuoteResponse[].class);

            if (quoteResponse != null && quoteResponse.length > 0) {
                FmpStockQuoteResponse quote = quoteResponse[0];

                double currentPrice = quote.getPrice();
                double priceChange = quote.getChange();
                double percentageChange = quote.getChangesPercentage();

                Stock stock = new Stock();
                stock.setSymbol(retrievedSymbol.toUpperCase());
                stock.setCurrentPrice(currentPrice);
                stock.setPercentageChange(percentageChange);
                stock.setPriceChange(priceChange);
                stock.setName(name);
                return stockRepository.save(stock);
            } else {
                throw new RuntimeException("Failed to fetch stock quote for symbol: " + symbol);
            }
        } else {
            throw new RuntimeException("No match found for symbol: " + symbol);
        }
    }

    public List<StockHistory> getHistoricalStockData(String symbol, LocalDate startDate, LocalDate endDate) {
        List<StockHistory> stockHistoryList = stockHistoryRepository.findBySymbolAndDateBetween(symbol, Date.valueOf(startDate), Date.valueOf(endDate));
        if (stockHistoryList.isEmpty()) {
            stockHistoryList = fetchAndStoreHistoricalStockData(symbol);
        }
        return stockHistoryList;
    }

    public List<StockHistory> fetchAndStoreHistoricalStockData(String symbol) {
        String url = FMP_BASE_URL + "/historical-price-full/" + symbol + "?timeseries=52&apikey=" + fmpApiKey;
        RestTemplate restTemplate = new RestTemplate();
        FmpHistoricalResponse response = restTemplate.getForObject(url, FmpHistoricalResponse.class);

        if (response != null && response.getHistoricalData() != null) {
            List<StockHistory> stockHistoryList = new ArrayList<>();

            int count = 0;
            for (FmpHistoricalData entry : response.getHistoricalData()) {
                Date currentDate = Date.valueOf((entry.getDate()));

                if (currentDate != null) {
                    StockHistory stockHistory = new StockHistory();
                    stockHistory.setDate(currentDate);
                    stockHistory.setClose(entry.getClose());
                    stockHistory.setSymbol(symbol);
                    stockHistoryList.add(stockHistory);
                    count++;
                }

                if (count >= 52) {
                    break;
                }
            }

            return stockHistoryRepository.saveAll(stockHistoryList);
        } else {
            throw new RuntimeException("Failed to fetch historical data for symbol: " + symbol);
        }
    }


    // stock id: MSFT - 23
    public void checkAndRemoveStockIfNotMonitored(Stock stock) {
        if (!isStockMonitoredByOtherUsers(stock.getId())) {
            stockHistoryRepository.deleteBySymbol(stock.getSymbol());
            stockRepository.delete(stock);
        }
    }

    public boolean isStockMonitoredByOtherUsers(Long stockId) {
        return userRepository.existsByMonitoredStockIdsContains(stockId.toString());
    }

    @Scheduled(cron = "0 * * * *") // Runs every hour
    public void refreshAllStockData() {
        List<Stock> allStocks = stockRepository.findAll();
        for (Stock stock : allStocks) {
            fetchAndStoreStockInfo(stock.getSymbol());
            fetchAndStoreHistoricalStockData(stock.getSymbol());
        }
    }
}
