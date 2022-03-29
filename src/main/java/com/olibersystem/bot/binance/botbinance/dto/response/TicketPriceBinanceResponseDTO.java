package com.olibersystem.bot.binance.botbinance.dto.response;

import com.olibersystem.bot.binance.botbinance.dto.ErrorDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TicketPriceBinanceResponseDTO {
    private List<TickerPriceDto> data;
    private ErrorDTO errorDTO;
}
