package com.olibersystem.bot.binance.botbinance.dto.response;

import com.olibersystem.bot.binance.botbinance.dto.DTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SymbolDto implements DTO {
    private final String name;
    private final Double average;

}
