package com.buildsessions.stockmarketanalyzer.service;

import com.buildsessions.stockmarketanalyzer.entity.Stock;
import com.buildsessions.stockmarketanalyzer.model.AlphaVantageSearchResponse;
import com.buildsessions.stockmarketanalyzer.model.AlphaVantageSymbolMatch;
import com.buildsessions.stockmarketanalyzer.repository.StockRepository;
import com.buildsessions.stockmarketanalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockService {


    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserRepository userRepository;

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
        String searchUrl = ALPHA_VANTAGE_BASE_URL + "?function=SYMBOL_SEARCH&keywords=" + symbol + "&apiKey=" + alphaVantageApiKey;
        RestTemplate restTemplate = new RestTemplate();
        AlphaVantageSearchResponse searchResponse = restTemplate.getForObject(searchUrl, AlphaVantageSearchResponse.class);

        if (searchResponse != null && !searchResponse.getBestMatches().isEmpty()) {
            AlphaVantageSymbolMatch symbolMatch = searchResponse.getBestMatches().get(0);
            String retrievedSymbol = symbolMatch.getSymbol();
            String name = symbolMatch.getName();

        }
    }

}
