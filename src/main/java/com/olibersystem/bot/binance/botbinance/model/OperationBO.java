package com.olibersystem.bot.binance.botbinance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OperationBO {
    private LocalDateTime dateOperation;
    private Double price;
}
