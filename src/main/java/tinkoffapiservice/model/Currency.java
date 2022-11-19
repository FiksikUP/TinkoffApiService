package tinkoffapiservice.model;

public enum Currency {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR");

    private String currency;

    Currency(String currency) {
        this.currency = currency;
    }
}
