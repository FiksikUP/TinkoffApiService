package tinkoffapiservice.service;

import tinkoffapiservice.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
}
