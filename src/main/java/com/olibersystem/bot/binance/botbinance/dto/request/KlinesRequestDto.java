package com.olibersystem.bot.binance.botbinance.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@Builder
public class KlinesRequestDto {
    private LocalDateTime openTime;
    private double open;
    private double high;
    private double  low;
    private double close;
    private double volume;
    private LocalDateTime closeTime;
    private double quoteAssetVolume;
    private long numberOfTrades;
    private double takerBuyBaseAssetVolume;
    private double takerBuyQuoteAssetVolume;
    private double ignore;
    public static KlinesRequestDto generate(List<Object> obj) {
        return KlinesRequestDto.builder()
                .openTime(LocalDateTime.ofEpochSecond( Long.valueOf(obj.get(0).toString())/ 1000,
                        0, ZoneOffset.UTC))
                .open(Double.valueOf( String.valueOf( obj.get(1) )) )
                .high(Double.valueOf( String.valueOf( obj.get(2) )) )
                .low(Double.valueOf( String.valueOf( obj.get(3) )) )
                .close(Double.valueOf( String.valueOf( obj.get(4) )))
                .volume(Double.valueOf( String.valueOf( obj.get(5) )))
                .closeTime(LocalDateTime.ofEpochSecond( Long.valueOf(obj.get(6).toString())/ 1000,
                        0, ZoneOffset.UTC))
                .quoteAssetVolume(Double.valueOf( String.valueOf( obj.get(7) )))
                .numberOfTrades(Long.valueOf( String.valueOf( obj.get(8) )))
                .takerBuyBaseAssetVolume(Double.valueOf( String.valueOf( obj.get(9) )))
                .takerBuyQuoteAssetVolume(Double.valueOf( String.valueOf( obj.get(10) )))
                .ignore(Double.valueOf( String.valueOf( obj.get(11) )))
                .build();
    }

    public String toString() {
        return this.getOpenTime() + ","+ this.getOpen()+ ","+this.getHigh()+ ","+this.getLow()+ ","+this.getClose()+ ","+
                this.getVolume()+ ","+this.getCloseTime()+ ","+this.getQuoteAssetVolume()+ ","+this.getNumberOfTrades()+ ","+
                this.getTakerBuyBaseAssetVolume()+ ","+this.getTakerBuyQuoteAssetVolume()+ ","+this.getIgnore();
    }
}
