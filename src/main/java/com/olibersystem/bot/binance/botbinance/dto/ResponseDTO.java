package com.olibersystem.bot.binance.botbinance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO implements DTO{

    private ErrorDTO error;
}
