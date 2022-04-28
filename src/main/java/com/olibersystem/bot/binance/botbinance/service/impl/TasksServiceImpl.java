package com.olibersystem.bot.binance.botbinance.service.impl;

import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.KlinesRequestDto;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import com.olibersystem.bot.binance.botbinance.service.BinanceService;
import com.olibersystem.bot.binance.botbinance.service.KlintesServices;
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
    private final KlintesServices klintesServices;

    public TasksServiceImpl(BinanceService binanceService, MotorDataService motorDataService, KlintesServices klintesServices) {
        this.binanceService = binanceService;
        this.motorDataService = motorDataService;
        this.klintesServices = klintesServices;
    }

    @Override
    public void loadData() {
        try {
            exchangeInfoProcess(binanceService.exchangeInfo());
            priceProcess(binanceService.pricesInfo());
            klintesProcess();
        } catch (Exception e) {
            log.error("Error general: "+e.getMessage(), e);
        }
    }

    private void klintesProcess() {
        motorDataService.getAllInstrument()
                .stream()
                .filter(fill->"USDT".equals(fill.getInstrument().getQuoteAsset()))
                .forEach( f -> {
                            this.findKlines(f.getInstrument().getSymbol());
                            klintesServices.printAlert(f.getInstrument().getSymbol());
                        });
    }

    private void findKlines(String instrument) {
        try {
            List<KlinesRequestDto> listt = binanceService.klines(instrument, "15m");
            klintesServices.add(instrument, listt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
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
