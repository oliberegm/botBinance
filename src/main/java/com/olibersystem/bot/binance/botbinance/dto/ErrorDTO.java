package com.olibersystem.bot.binance.botbinance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO implements DTO {
    private UUID id;
    private String message;
}
