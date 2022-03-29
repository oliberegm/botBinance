package com.olibersystem.bot.binance.botbinance.service.impl;

import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import com.olibersystem.bot.binance.botbinance.service.BinanceService;
import com.olibersystem.bot.binance.botbinance.service.MotorDataService;
import com.olibersystem.bot.binance.botbinance.service.TasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TasksServiceImpl implements TasksService {
    private final BinanceService binanceService;
    private final MotorDataService motorDataService;

    public TasksServiceImpl(BinanceService binanceService, MotorDataService motorDataService) {
        this.binanceService = binanceService;
        this.motorDataService = motorDataService;
    }

    @Override
    public void loadData() {
        exchangeInfoProcess(binanceService.exchangeInfo());
        priceProcess(binanceService.pricesInfo());
    }

    private void exchangeInfoProcess(ExchangeInfoDTO success) {
        success.getSymbols().stream()
                .forEach( f -> motorDataService.add(f));
    }
    private void priceProcess(List<TickerPriceDto> success) {
        success.stream()
                .forEach(f -> motorDataService.add(f));
    }
}
