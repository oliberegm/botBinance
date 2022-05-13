package com.olibersystem.bot.binance.botbinance.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTickerDTO {
    private String symbol;
    private String bidPrice; // precio para compra
    private String bidQty;
    private String askPrice; // precio para vender
    private String askQty;
}
