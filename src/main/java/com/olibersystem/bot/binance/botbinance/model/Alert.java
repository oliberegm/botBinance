package com.olibersystem.bot.binance.botbinance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alert {
    private String instrument;
    private boolean valid;
    private double increment;
    private LocalDateTime executionTime;
    private LocalDateTime encounter;
    public String toString(){
        return String.format("%10s, %s, %6.2f, %s, %s", instrument, valid, increment, executionTime, encounter);
    }
}
