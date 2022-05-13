package com.olibersystem.bot.binance.botbinance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Pruebas {
    public static void main1(String[] args) {
        /*double number1 = 100/6.12000000;
        double round = 0.00010000;
        int count = 0;
        while(round < 1){
            round *= 10;
            count++;
        }
        double number2 = (int)(Math.round(number1 * Math.pow(10, count)))/Math.pow(10, count);
        System.out.println(number2);*/
        Double a = 30.152535;
        Double amount = 30.0000000;

        System.out.println(BigDecimal.valueOf(a).setScale(1, RoundingMode.FLOOR).doubleValue());
        System.out.println(BigDecimal.valueOf(a).setScale(2, RoundingMode.FLOOR).doubleValue());
        System.out.println(BigDecimal.valueOf(a).setScale(3, RoundingMode.FLOOR).doubleValue());
        System.out.println(BigDecimal.valueOf(a).setScale(4, RoundingMode.FLOOR).doubleValue());
        System.out.println(BigDecimal.valueOf(a).setScale(5, RoundingMode.FLOOR).doubleValue());

        LocalDateTime dt = LocalDateTime.ofEpochSecond(1648312440000L/ 1000,0, ZoneOffset.UTC);
        System.out.println(dt.toString());
        dt = LocalDateTime.ofEpochSecond( 1648343520000L/ 1000,0, ZoneOffset.UTC);
        System.out.println(dt.toString());
        dt = LocalDateTime.ofEpochSecond(1648343579999L/ 1000,0, ZoneOffset.UTC);
        System.out.println(dt.toString());

    }
}
