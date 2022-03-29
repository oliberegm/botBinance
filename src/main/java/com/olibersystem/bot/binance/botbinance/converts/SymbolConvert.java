package com.olibersystem.bot.binance.botbinance.converts;
import com.olibersystem.bot.binance.botbinance.dto.response.SymbolDto;
import com.olibersystem.bot.binance.botbinance.model.SymbolBO;

public class SymbolConvert {
    public static SymbolDto convertToDto(SymbolBO entity) {
        return SymbolDto.builder()
                .name(entity.getSymbol())
                .average(entity.getAverage())
                .build();
    }

}
