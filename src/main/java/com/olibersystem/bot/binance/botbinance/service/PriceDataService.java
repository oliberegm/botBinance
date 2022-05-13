package com.olibersystem.bot.binance.botbinance.service;

import com.olibersystem.bot.binance.botbinance.dto.request.BookTickerDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.model.SymbolBO;
import com.olibersystem.bot.binance.botbinance.model.TraderBO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceDataService {
    private Map<String, TraderBO> data;

    PriceDataService() {
        data = new HashMap<>();
    }

    public void add(List<ExchangeInfoDTO.SymbolRequestDto> symbols) {
        symbols.forEach(f -> add(f));
    }

    public void add(ExchangeInfoDTO.SymbolRequestDto symbol) {
        TraderBO traderBO = data.get(symbol.getSymbol());
        if(traderBO == null) {
            traderBO = TraderBO.builder()
                    .instrument(symbol.getSymbol())
                    .base(symbol.getBaseAsset())
                    .buy(-1D)
                    .sell(-1D)
                    .price(-1D)
                    .operated("TRADING".equals(symbol.getStatus()))
                    .quote(symbol.getQuoteAsset())
                    .stepSize(symbol.getFilters().stream()
                            .filter(f -> "LOT_SIZE".equals(f.getFilterType()))
                            .findFirst().map( m -> Double.valueOf(m.getStepSize()) )
                            .orElse(1D))
                    .tickSize(symbol.getFilters().stream()
                            .filter(f -> "PRICE_FILTER".equals(f.getFilterType()))
                            .findFirst().map( m -> Double.valueOf(m.getTickSize()) )
                            .orElse(1D))
                    .build();
        }
        data.put(symbol.getSymbol(), traderBO);
    }

    public void update(List<BookTickerDTO> tickerPriceDto) {
        tickerPriceDto.forEach(f -> update(f) );
    }

    private void update(BookTickerDTO tickerPriceDto) {
        TraderBO traderBO = data.get(tickerPriceDto.getSymbol());
        if(traderBO != null) {
            traderBO.setSell(Double.valueOf(tickerPriceDto.getAskPrice()));
            traderBO.setBuy(Double.valueOf(tickerPriceDto.getBidPrice()));
        }
        data.put(tickerPriceDto.getSymbol(), traderBO);
    }

    public TraderBO get(String symbol) {
        return data.get(symbol);
    }

    public List<TraderBO> getAll() {
        List<TraderBO> list =
                data.entrySet().stream()
                        .map(Map.Entry::getValue)
                        .collect(Collectors.toList());
        return list;
    }
    public List<TraderBO> getTrading() {
        return getAll().stream()
                .filter(f->f.isOperated())
                .collect(Collectors.toList());
    }

}
