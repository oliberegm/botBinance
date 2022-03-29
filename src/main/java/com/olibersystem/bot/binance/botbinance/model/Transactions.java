package com.olibersystem.bot.binance.botbinance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

//@AllArgsConstructor
@Builder
@Data
@ToString
public class Transactions {
    private DetailsOperation op1;
    private DetailsOperation op2;
    private DetailsOperation op3;
    private Double free;
}
