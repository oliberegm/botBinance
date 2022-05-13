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
    private double price;

    public String toString(){
        return String.format("%10s, %s, %6.2f, %19s, %19s, entrada: %5.6f, salida: %5.6f", instrument, valid, increment, executionTime, encounter, price, getSell() );
    }
    public double getSell() {
        return price * 1.008;
    }
}
