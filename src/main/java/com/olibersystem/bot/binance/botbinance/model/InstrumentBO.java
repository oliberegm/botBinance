package com.olibersystem.bot.binance.botbinance.model;

import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Builder
@Data
public class InstrumentBO {
    private ExchangeInfoDTO.SymbolRequestDto instrument;
    private Double price;

    public Double amountOperation(String symbol, Double amount) {
        if ( !this.instrument.getBaseAsset().equals(symbol) ) {
            return getBuy(amount);
        } else {
            return getSell(amount);
        }
    }

    public String typeOperation(String symbol) {
        if ( !this.instrument.getBaseAsset().equals(symbol) ) {
            return "BUY";
        } else {
            return "SELL";
        }
    }

    public String getSymbol(String symbol) {
        if ( !this.instrument.getBaseAsset().equals(symbol) ) {
            return this.instrument.getBaseAsset();
        } else {
            return this.instrument.getQuoteAsset();
        }
    }

    public Double getBuy(Double amount) {
        return roundLotSize(amount / this.price);
    }

    public String getBuy() {
        return this.instrument.getBaseAsset();
    }

    public Double getSell(Double amount) {
        return roundLotSize(amount * price);
    }

    public String getSell() {
        return this.instrument.getQuoteAsset();
    }

    public double discount() {
        return Double.parseDouble(instrument.getFilters().stream()
                .filter(f -> f.getFilterType().equals("LOT_SIZE"))
                .findFirst()
                .get().getStepSize());
    }

    public Double evaluatePriceFilter(Double amount) {
        return this.roundPriceFilter(amount);
    }

    public Double evaluateAmount(Double amount) {
        return this.roundLotSize(amount);
    }

    private Double roundLotSize(Double amount) {
        double number1 = amount;
        double round = Double.parseDouble(instrument.getFilters().stream()
                .filter(f -> f.getFilterType().equals("LOT_SIZE"))
                .findFirst()
                .get().getStepSize());
        int count = 0;
        while(round < 1){
            round *= 10;
            count++;
        }
        return BigDecimal.valueOf(amount).setScale(count, RoundingMode.FLOOR).doubleValue();
    }
    private Double roundPriceFilter(Double amount) {
        double number1 = amount;
        double round = Double.parseDouble(instrument.getFilters().stream()
            .filter(f -> f.getFilterType().equals("PRICE_FILTER"))
            .findFirst()
            .get().getTickSize());
        int count = 0;
        while(round < 1){
            round *= 10;
            count++;
        }
        return BigDecimal.valueOf(amount).setScale(count, RoundingMode.FLOOR).doubleValue();
    }
}
