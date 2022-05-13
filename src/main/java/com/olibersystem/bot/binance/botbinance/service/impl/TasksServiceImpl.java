package com.olibersystem.bot.binance.botbinance.service.impl;

import com.olibersystem.bot.binance.botbinance.dto.request.BookTickerDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.Info24HDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.KlinesRequestDto;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import com.olibersystem.bot.binance.botbinance.service.BinanceService;
import com.olibersystem.bot.binance.botbinance.service.BinariesService;
import com.olibersystem.bot.binance.botbinance.service.InstrumentService;
import com.olibersystem.bot.binance.botbinance.service.KlintesServices;
import com.olibersystem.bot.binance.botbinance.service.MotorDataService;
import com.olibersystem.bot.binance.botbinance.service.PriceDataService;
import com.olibersystem.bot.binance.botbinance.service.TasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TasksServiceImpl implements TasksService {
    private final BinanceService binanceService;
    private final BinariesService binariesService;
    private final MotorDataService motorDataService;
    private final KlintesServices klintesServices;
    private final InstrumentService instrumentService;
    private final PriceDataService priceDataService;

    public TasksServiceImpl(BinanceService binanceService, BinariesService binariesService, MotorDataService motorDataService, KlintesServices klintesServices,
                            InstrumentService instrumentService, PriceDataService priceDataService) {
        this.binanceService = binanceService;
        this.binariesService = binariesService;
        this.motorDataService = motorDataService;
        this.klintesServices = klintesServices;
        this.instrumentService = instrumentService;
        this.priceDataService = priceDataService;
    }

    @Override
    public void loadData() {
        try {
            exchangeInfoProcess(binanceService.exchangeInfo());
            priceProcess(binanceService.pricesInfo());
            pricePrecess(binanceService.bookTicker());
            info24hProcess(binanceService.Info24H());
        } catch (Exception e) {
            log.error("Error general: "+e.getMessage(), e);
        }
    }

    @Override
    public void klintesProcess() {
        klintesServices.generateMoney();
    }

    @Override
    public void binariesProcess() {
        // todo hay q arreglar la manera como calcula el camino no sirve el price
        // hay que usar /api/v3/ticker/bookTicker
        binariesService.generateMoney();
        //instrumentService.executeThreeOperation();
    }

    private void exchangeInfoProcess(ExchangeInfoDTO success) {
        success.getSymbols().stream()
                .forEach( f -> motorDataService.add(f));
        priceDataService.add(success.getSymbols());
    }
    private void priceProcess(List<TickerPriceDto> success) {
        success.stream()
                .forEach(f -> motorDataService.add(f));
    }
    private void info24hProcess(List<Info24HDTO> success) {
        motorDataService.addInfo(success);
    }

    private void pricePrecess(List<BookTickerDTO> success) {
        priceDataService.update(success);
    }
}
