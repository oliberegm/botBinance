package com.olibersystem.bot.binance.botbinance.model;

import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@ToString
public class TraderBO {
    private String instrument;
    private String base;
    private String quote;
    private Double buy;
    private Double sell;
    private boolean operated;
    private Double price;
    private double stepSize;
    private double tickSize;

    /*public TraderBO(ExchangeInfoDTO.SymbolRequestDto symbolBO) {
        this.instrument = symbolBO.getSymbol();
        this.base = symbolBO.getBaseAsset();
        this.quote = symbolBO.getQuoteAsset();
        this.operated = "TRADING".equals(symbolBO.getStatus());
        buy = 1D;
        sell = 1D;
    }*/

    public Double round(Double amount, int type) {
        double round = type == 1 ? stepSize: tickSize;
        int count = 0;
        while(round < 1){
            round *= 10;
            count++;
        }
        return BigDecimal.valueOf(amount).setScale(count, RoundingMode.FLOOR).doubleValue();
    }
}
