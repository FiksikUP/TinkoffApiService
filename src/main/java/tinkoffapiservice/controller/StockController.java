package tinkoffapiservice.controller;

import org.springframework.web.bind.annotation.*;
import tinkoffapiservice.dto.StockPrice;
import tinkoffapiservice.model.Stock;
import tinkoffapiservice.service.StockService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StockController {
    private final StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker){
        return stockService.getStockByTicker(ticker);
    }

    @PostMapping("/stocks")
    public List<Stock> getStocks(@RequestBody List<String> tickers){
        return stockService.getStocksByTickers(tickers);
    }

    @PostMapping("/prices")
    public List<StockPrice> getPrices(@RequestBody List<String> figies){
        return stockService.getPricesStocksByFigies(figies);
    }
}
