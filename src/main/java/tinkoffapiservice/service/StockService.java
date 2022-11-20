package tinkoffapiservice.service;

import tinkoffapiservice.dto.StockPrice;
import tinkoffapiservice.model.Stock;

import java.util.List;

public interface StockService {
    Stock getStockByTicker(String ticker);
    List<Stock> getStocksByTickers(List<String> tickers);
    List<StockPrice> getPricesStocksByFigies(List<String> figies);
}
