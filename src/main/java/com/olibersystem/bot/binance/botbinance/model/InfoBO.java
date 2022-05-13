package com.olibersystem.bot.binance.botbinance.model;

import com.olibersystem.bot.binance.botbinance.dto.request.BookTickerDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.Info24HDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InfoBO {
    private Info24HDTO info24HDTO;
    private TickerPriceDto tickerPriceDto;
    private BookTickerDTO bookTickerDTO;
    private ExchangeInfoDTO.SymbolRequestDto symbolRequestDto;
}
