package com.olibersystem.bot.binance.botbinance.model;

import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class SymbolBO {
    private String idName;
    private String symbol;
    private SummaryBO summaryBO;
    private double average;
    private TraderBO traderBO;
    private List<OperationBO> operations;

    public SymbolBO(ExchangeInfoDTO.SymbolRequestDto symbol) {
        this.idName = symbol.getSymbol();
        this.symbol = symbol.getSymbol();
        this.traderBO = new TraderBO(symbol);
        summaryBO = new SummaryBO();
        operations = new ArrayList<>();
    }
    public SymbolBO(String symbol) {
        this.idName = symbol;
        this.symbol = symbol;
        summaryBO = new SummaryBO();
        operations = new ArrayList<>();
    }
    public void addOperation(TickerPriceDto tickerPriceDto) {
        OperationBO operationBO = new OperationBO(LocalDateTime.now(),
                Double.parseDouble(tickerPriceDto.price));
        operations.add(operationBO);
        this.summaryBO.update(operationBO);
        this.traderBO.setPrice(operationBO.getPrice());
        this.traderBO.setBuy(operationBO.getPrice());
        this.traderBO.setSell(1 / operationBO.getPrice());
        this.generateCalculations();
    }

    private void generateCalculations() {
        average = this.operations.stream()
                .mapToDouble(OperationBO::getPrice)
                .average().orElse(0);
    }

    @Override
    public String toString() {
        return "SymbolBO{" +
                ", symbol='" + symbol + '\'' +
                ", summaryBO=" + summaryBO +
                ", average=" + average +
                '}';
    }
}
