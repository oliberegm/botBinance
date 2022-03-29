package com.olibersystem.bot.binance.botbinance.service;

import com.olibersystem.bot.binance.botbinance.converts.SymbolConvert;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import com.olibersystem.bot.binance.botbinance.dto.response.SymbolDto;
import com.olibersystem.bot.binance.botbinance.model.InstrumentBO;
import com.olibersystem.bot.binance.botbinance.model.SymbolBO;
import com.olibersystem.bot.binance.botbinance.model.TraderBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MotorDataService {
    private Map<String, SymbolBO> data;
    private Map<String, InstrumentBO> instrumentBOMap;

    MotorDataService() {
        data = new HashMap<>();
        instrumentBOMap = new HashMap<>();
    }

    public void add(ExchangeInfoDTO.SymbolRequestDto dto) {
        data.put(dto.getSymbol(), Optional
                            .ofNullable(data.get(dto.getSymbol()))
                            .orElse(new SymbolBO(dto))
                );
        instrumentBOMap.put(dto.getSymbol(),
                Optional
                        .ofNullable(instrumentBOMap.get(dto.getSymbol()))
                        .orElse(InstrumentBO.builder().instrument(dto).build()));
    }

    public void add(TickerPriceDto priceDto) {
        SymbolBO symbolBO = data.get(priceDto.symbol);
        InstrumentBO instrumentBO = instrumentBOMap.get(priceDto.symbol);
        if (symbolBO == null || instrumentBO == null) {
            log.error(String.format("NO FIND SYMBOL %s", priceDto.symbol));
            return;
        }
        symbolBO.addOperation(priceDto);
        data.put(priceDto.symbol, symbolBO);
        instrumentBO.setPrice(Double.parseDouble(priceDto.price));
        instrumentBOMap.put(priceDto.symbol, instrumentBO);
    }

    public List<SymbolBO> getAll() {
        List<SymbolBO> list =
            data.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        return list;
    }
    public List<String> getAllNames() {
        List<String> list =
                data.entrySet().stream()
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
        return list;
    }

    public List<SymbolDto> getAllOrder() {
        return getAll().stream()
                .sorted(Comparator.comparing(SymbolBO::getAverage))
                .map(SymbolConvert::convertToDto)
                .collect(Collectors.toList());
    }
    public List<TraderBO> getTrading() {
        return getAll().stream()
                .map(SymbolBO::getTraderBO)
                .filter(f->f.isStatus())
                .collect(Collectors.toList());
    }

    public List<TraderBO> getTraderBySymbol(String symbol) {
        return getAll().stream()
                .map(SymbolBO::getTraderBO)
                .filter(f->f.isStatus())
                .filter(f->f.getQuote().equals(symbol) || f.getBase().equals(symbol) )
                .collect(Collectors.toList());
    }
    public List<TraderBO> getTraderBySymbolNoUSDT(String symbol) {
        return getAll().stream()
                .map(SymbolBO::getTraderBO)
                .filter(f->f.isStatus())
                .filter(f->(f.getQuote().equals(symbol) || f.getBase().equals(symbol)) && !(f.getQuote().equals("USDT") || f.getBase().equals("USDT")) )
                .collect(Collectors.toList());
    }
    public List<TraderBO> getTraderBySymbol(String symbol, String symbol2) {
        return getAll().stream()
                .map(SymbolBO::getTraderBO)
                .filter(f->f.isStatus())
                .filter(f->f.getQuote().equals(symbol) || f.getBase().equals(symbol) )
                .filter(f->f.getQuote().equals(symbol2) || f.getBase().equals(symbol2) )
                .collect(Collectors.toList());
    }

    public List<InstrumentBO> getAllInstrument() {
        List<InstrumentBO> list =
                instrumentBOMap.entrySet().stream()
                        .map(Map.Entry::getValue)
                        .collect(Collectors.toList());
        return list;
    }

    public List<InstrumentBO> getTraderBySymbolInstrument(String symbol) {
        return getAllInstrument().stream()
                .filter(f->f.getInstrument().getStatus().equals("TRADING"))
                .filter(f->f.getInstrument().getQuoteAsset().equals(symbol) )
                .collect(Collectors.toList());
    }
    public List<InstrumentBO> getTraderBySymbolInstrument(String symbol, String symbol2) {
        return getAllInstrument().stream()
                .filter(f->f.getInstrument().getStatus().equals("TRADING"))
                .filter(f->f.getInstrument().getBaseAsset().equals(symbol)
                        && f.getInstrument().getQuoteAsset().equals(symbol2) )
                .collect(Collectors.toList());
    }
    public List<InstrumentBO> getTraderBySymbolInstrumentNoUSDT(String symbol) {
        return getAllInstrument().stream()
                .filter(f->f.getInstrument().getStatus().equals("TRADING"))
                .filter(f->f.getInstrument().getBaseAsset().equals(symbol)
                        && !(f.getInstrument().getQuoteAsset().equals("USDT") || f.getInstrument().getBaseAsset().equals("USDT")) )
                .collect(Collectors.toList());
    }

    public InstrumentBO getInstrumentByInstrument(String instrument) {
        return getAllInstrument().stream()
                .filter(f->f.getInstrument().getSymbol().equals(instrument) )
                .findFirst().get();
    }
}
