package com.olibersystem.bot.binance.botbinance.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class TickerPriceDto {
    public final String symbol;
    public final String price;

    @JsonCreator
    public TickerPriceDto(@JsonProperty("symbol") String symbol,
                          @JsonProperty("price") String price) {
        this.symbol = symbol;
        this.price = price;
    }
}
