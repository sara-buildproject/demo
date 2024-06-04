package com.buildsessions.stockmarketanalyzer.service;

import com.buildsessions.stockmarketanalyzer.entity.Stock;
import com.buildsessions.stockmarketanalyzer.entity.StockHistory;
import com.buildsessions.stockmarketanalyzer.model.AlphaVantageGlobalQuote;
import com.buildsessions.stockmarketanalyzer.model.AlphaVantageGlobalQuoteResponse;
import com.buildsessions.stockmarketanalyzer.model.AlphaVantageSearchResponse;
import com.buildsessions.stockmarketanalyzer.model.AlphaVantageSymbolMatch;
import com.buildsessions.stockmarketanalyzer.repository.StockHistoryRepository;
import com.buildsessions.stockmarketanalyzer.repository.StockRepository;
import com.buildsessions.stockmarketanalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StockService {


    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Value("${alpha.vantage.api.key}")
    private String alphaVantageApiKey;

    private final String ALPHA_VANTAGE_BASE_URL = "https://www.alphavantage.co/query";

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

    public Stock fetchAndStoreStockInfo(String symbol) {
        String searchUrl = ALPHA_VANTAGE_BASE_URL + "?function=SYMBOL_SEARCH&keywords=" + symbol + "&apikey=" + alphaVantageApiKey;
        RestTemplate restTemplate = new RestTemplate();
        AlphaVantageSearchResponse searchResponse = restTemplate.getForObject(searchUrl, AlphaVantageSearchResponse.class);

        if (searchResponse != null && !searchResponse.getBestMatches().isEmpty()) {
            AlphaVantageSymbolMatch symbolMatch = searchResponse.getBestMatches().get(0);
            String retrievedSymbol = symbolMatch.getSymbol();
            String name = symbolMatch.getName();

            String quoteUrl = ALPHA_VANTAGE_BASE_URL + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + alphaVantageApiKey;
            AlphaVantageGlobalQuoteResponse quoteResponse = restTemplate.getForObject(quoteUrl, AlphaVantageGlobalQuoteResponse.class);

            if (quoteResponse != null && quoteResponse.getGlobalQuote() != null) {
                AlphaVantageGlobalQuote globalQuote = quoteResponse.getGlobalQuote();

                double currentPrice = Double.parseDouble(globalQuote.getCurrentPrice());
                double priceChange = Double.parseDouble(globalQuote.getPriceChange());
                double percentageChange = Double.parseDouble(globalQuote.getPercentageChange().replace("%", ""));

                Stock stock = new Stock();
                stock.setSymbol(retrievedSymbol.toUpperCase());
                stock.setCurrentPrice(currentPrice);
                stock.setPercentageChange(percentageChange);
                stock.setPriceChange(priceChange);
                stock.setName(name);

                return stockRepository.save(stock);
            } else {
                throw new RuntimeException("Failed to fetch global quote for the requested symbol: " + retrievedSymbol);
            }

        } else {
            throw new RuntimeException("No match found for the requested symbol: " + symbol);
        }
    }

    public List<StockHistory> getHistoricalStockData(String symbol, LocalDate startDate, LocalDate endDate) {
        List<StockHistory> stockHistoryList = stockHistoryRepository.findBySymbolAndDateBetween(symbol, Date.valueOf(startDate), Date.valueOf(endDate));
        if (stockHistoryList.isEmpty()) {
            stockHistoryList = fetchAndStoreHistoricalStockData(symbol, Date.valueOf(startDate), Date.valueOf(endDate));
        }
        return stockHistoryList;
    }

    public List<StockHistory> fetchAndStoreHistoricalStockData(String symbol, Date startDate, Date endDate) {
        String url = ALPHA_VANTAGE_BASE_URL + "?function=TIME_SERIES_WEEKLY&symbol=" + symbol + "&apikey=" + alphaVantageApiKey;
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("Weekly Time Series")) {
            Map<String, Map<String, String>> timeSeries = (Map<String, Map<String, String>>) response.get("Weekly Time Series");
            List<StockHistory> stockHistoryList = new ArrayList<>();

            for (Map.Entry<String, Map<String, String>> entry : timeSeries.entrySet()) {
                Date currentDate = Date.valueOf(entry.getKey());
                if (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0) {
                    StockHistory stockHistory = new StockHistory();
                    stockHistory.setDate(Date.valueOf(entry.getKey()));
                    stockHistory.setClose(Double.parseDouble(entry.getValue().get("4. close")));
                    stockHistory.setSymbol(symbol);
                    stockHistoryList.add(stockHistory);
                }
            }
            return stockHistoryRepository.saveAll(stockHistoryList);
        } else {
            throw new RuntimeException("Failed to fetch historical data for the requested symbol: " + symbol);
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

}
