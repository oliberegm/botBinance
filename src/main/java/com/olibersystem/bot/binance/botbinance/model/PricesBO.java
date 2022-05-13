package com.olibersystem.bot.binance.botbinance.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricesBO {
    private String symbol;
    private Double bidPrice; // precio para compra
    private Double askPrice; // precio para vender

}
