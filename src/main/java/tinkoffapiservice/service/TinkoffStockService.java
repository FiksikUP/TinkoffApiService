package tinkoffapiservice.service;

import org.springframework.scheduling.annotation.Async;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;
import tinkoffapiservice.dto.StockPrice;
import tinkoffapiservice.exception.StockNotFoundException;
import tinkoffapiservice.model.Currency;
import tinkoffapiservice.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService{
    private final OpenApi openApi;

    @Async
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentTicker(String ticker) {
        return openApi.getMarketContext().searchMarketInstrumentsByTicker(ticker);
    }
    @Override
    public Stock getStockByTicker(String ticker) {
        var instruments = getMarketInstrumentTicker(ticker).join().getInstruments();
        if(instruments.isEmpty()){
            throw new StockNotFoundException(String.format("Stock %s - not found.", ticker));
        }
        var instrument = instruments.get(0);
        return new Stock(
                instrument.getTicker(),
                instrument.getFigi(),
                instrument.getName(),
                instrument.getType().getValue(),
                Currency.valueOf(instrument.getCurrency().getValue()),
                "TINKOFF");
    }
    @Override
    public List<Stock> getStocksByTickers(List<String> tickers){
        List<CompletableFuture<MarketInstrumentList>> marketInstruments = new ArrayList<>();
        tickers.forEach(ticker -> marketInstruments.add(getMarketInstrumentTicker(ticker)));
        return marketInstruments.stream()
                .map(CompletableFuture::join)
                .filter(instrument -> !instrument.getInstruments().isEmpty())
                .map(i -> {
                    var instrument = i.getInstruments().get(0);
                    return new Stock(
                            instrument.getTicker(),
                            instrument.getFigi(),
                            instrument.getName(),
                            instrument.getType().getValue(),
                            Currency.valueOf(instrument.getCurrency().getValue()),
                            "TINKOFF");
                })
                .collect(Collectors.toList());
    }

    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi){
        return openApi
                .getMarketContext()
                .getMarketOrderbook(figi, 0);
    }

    @Override
    public List<StockPrice> getPricesStocksByFigies(List<String> figies){
        List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
        figies.forEach(figi->orderBooks.add(getOrderBookByFigi(figi)));
        return orderBooks.stream()
                .map(CompletableFuture::join)
                .map(orderBook -> orderBook.orElseThrow(() -> new StockNotFoundException("Stock not found.")))
                .map(orderBook -> new StockPrice(
                        orderBook.getFigi(),
                        orderBook.getLastPrice().doubleValue()))
                .collect(Collectors.toList());
    }
}
