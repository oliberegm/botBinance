package com.olibersystem.bot.binance.botbinance.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class DetailsOperation {
    private String instrument;
    private Double cant;
    private String operation;
    private String symbol;
    private Double price;
}
