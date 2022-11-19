package tinkoffapiservice.service;

import tinkoffapiservice.exception.StockNotFoundException;
import tinkoffapiservice.model.Currency;
import tinkoffapiservice.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;

@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService{
    private final OpenApi openApi;

    @Override
    public Stock getStockByTicker(String ticker) {
        var context = openApi.getMarketContext();
        var instruments = context
                .searchMarketInstrumentsByTicker(ticker)
                .join()
                .getInstruments();
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
}
