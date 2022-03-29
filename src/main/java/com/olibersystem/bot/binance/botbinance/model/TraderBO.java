package com.olibersystem.bot.binance.botbinance.model;

import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TraderBO {
    private String instrument;
    private String base;
    private String quote;
    private Double buy;
    private Double sell;
    private boolean status;
    private Double price;

    public TraderBO(ExchangeInfoDTO.SymbolRequestDto symbolBO) {
        this.instrument = symbolBO.getSymbol();
        this.base = symbolBO.getBaseAsset();
        this.quote = symbolBO.getQuoteAsset();
        this.status = "TRADING".equals(symbolBO.getStatus());
        buy = 1D;
        sell = 1D;
    }
}
