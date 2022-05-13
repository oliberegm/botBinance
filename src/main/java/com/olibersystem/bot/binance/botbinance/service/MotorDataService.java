package com.olibersystem.bot.binance.botbinance.service;

import com.olibersystem.bot.binance.botbinance.converts.SymbolConvert;
import com.olibersystem.bot.binance.botbinance.dto.request.BookTickerDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.Info24HDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import com.olibersystem.bot.binance.botbinance.dto.response.SymbolDto;
import com.olibersystem.bot.binance.botbinance.model.InfoBO;
import com.olibersystem.bot.binance.botbinance.model.InstrumentBO;
import com.olibersystem.bot.binance.botbinance.model.SymbolBO;
import com.olibersystem.bot.binance.botbinance.model.TraderBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private Map<String, InfoBO> infoBOMap;

    MotorDataService() {
        data = new HashMap<>();
        instrumentBOMap = new HashMap<>();
        infoBOMap = new HashMap<>();
    }

    public void add(ExchangeInfoDTO.SymbolRequestDto dto) {
        SymbolBO symbolBO = data.get(dto.getSymbol());
        if ( symbolBO == null ) {
            symbolBO = new SymbolBO(dto);
        }
        data.put(dto.getSymbol(), symbolBO);
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

    public void add(BookTickerDTO bookTickerDTO) {
        SymbolBO symbolBO = data.get(bookTickerDTO.getSymbol());
        InstrumentBO instrumentBO = instrumentBOMap.get(bookTickerDTO.getSymbol());
        if (symbolBO == null || instrumentBO == null) {
            log.error(String.format("NO FIND SYMBOL %s", bookTickerDTO.getSymbol()));
            return;
        }
        symbolBO.addOperation(bookTickerDTO);
        data.put(bookTickerDTO.getSymbol(), symbolBO);
        instrumentBO.setPrice(Double.parseDouble(bookTickerDTO.getBidPrice()));
        instrumentBOMap.put(bookTickerDTO.getSymbol(), instrumentBO);
    }

    public void addStatistics() {


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
                .filter(f->f.isOperated())
                .collect(Collectors.toList());
    }

    public List<TraderBO> getTraderBySymbol(String symbol) {
        return getAll().stream()
                .map(SymbolBO::getTraderBO)
                .filter(f->f.isOperated())
                .filter(f->f.getQuote().equals(symbol) || f.getBase().equals(symbol) )
                .collect(Collectors.toList());
    }
    public List<TraderBO> getTraderBySymbolNoUSDT(String symbol) {
        return getAll().stream()
                .map(SymbolBO::getTraderBO)
                .filter(f->f.isOperated())
                .filter(f->(f.getQuote().equals(symbol) || f.getBase().equals(symbol)) && !(f.getQuote().equals("USDT") || f.getBase().equals("USDT")) )
                .collect(Collectors.toList());
    }
    public List<TraderBO> getTraderBySymbol(String symbol, String symbol2) {
        return getAll().stream()
                .map(SymbolBO::getTraderBO)
                .filter(f->f.isOperated())
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
    public List<InstrumentBO> getTraderBySymbolInstrument() {
        return getAllInstrument().stream()
                .filter(f->f.getInstrument().getStatus().equals("TRADING"))
                .filter(f->f.getInstrument().getQuoteAsset().equals("USDT") )
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


    public void addInfo(List<Info24HDTO> info24HDTOS) {
        info24HDTOS.forEach( f -> {
            InfoBO infoBO = infoBOMap.get(f.getSymbol());
            if(infoBO == null) {
                infoBO = InfoBO.builder().build();
            }
            infoBO.setInfo24HDTO(f);
            infoBOMap.put(f.getSymbol(), infoBO);
        });
    }

    public InfoBO getInfo(String symbol) {
        return infoBOMap.get(symbol);
    }



}
