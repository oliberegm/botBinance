package com.olibersystem.bot.binance.botbinance.service;

import com.olibersystem.bot.binance.botbinance.dto.request.KlinesRequestDto;
import com.olibersystem.bot.binance.botbinance.model.Alert;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnalisisVolumenService extends Thread{
    private static final int MAX_OPERATION = 1;
    private static final int MAX_CICLOS = 10;
    private static final String INTERVAL = "1m";
    private static final double SIZE_VOLUME = 4D;
    private static final Double MONTO_OPERACION = 20D;
    private final String symbol;
    private final BinanceService binanceService;
    private final MotorDataService motorDataService;

    public AnalisisVolumenService(String symbol, BinanceService binanceService, MotorDataService motorDataService) {
        this.symbol = symbol;
        this.binanceService = binanceService;
        this.motorDataService = motorDataService;
    }

    @Override
    public void run() {
        int i = 0;
        List<KlinesRequestDto> klines = findKlines();
        Alert alert = evaluateOptions(klines);
        executeOperation(alert);
        log.info("END CICLOS "+symbol);
    }

    private void executeOperation(Alert alert) {
        if (alert == null) return;
        log.error("ENCONTRADO SIMPLE: " + alert);
        buySell(alert);
    }

    public void buySell(Alert alert){
        double quantity = motorDataService.getInstrumentByInstrument(alert.getInstrument()).evaluateAmount( MONTO_OPERACION / alert.getPrice());
        double sell = motorDataService.getInstrumentByInstrument(alert.getInstrument()).evaluatePriceFilter(alert.getSell());
        if(1 != binanceService.newOrder(
            alert.getInstrument(),
            "BUY",
            alert.getPrice(),
            quantity)) {
            return;
        }
        try {
            validateOrderClose(alert.getInstrument());
        } catch (Exception e ) {}
        binanceService.newOrder(
            alert.getInstrument(),
            "SELL",
            sell,
            quantity
        );
    }

    private void validateOrderClose(String instrument) throws InterruptedException {
        while(binanceService.ordenOpen(instrument)) {
            Thread.sleep(500);
            log.error("NO SE HA CERRADO " + instrument);
        }
        log.error("CERRADA " + instrument);
    }



    private Alert evaluateOptions(List<KlinesRequestDto> klines) {
        Alert alert = null;
        KlinesRequestDto ant = klines.get(klines.size() - 2);
        KlinesRequestDto act = klines.get(klines.size() - 1);
        KlinesRequestDto cmp = klines.get(klines.size() - 5);
        if (ant.getOpen() < ant.getClose()
            && act.getOpen() < act.getClose()
            && act.getVolume()+ant.getVolume() > 30000d
            && cmp.getOpen() < act.getOpen()
            && act.getVolume() > ant.getVolume() * SIZE_VOLUME ) {
                alert = Alert.builder()
                        .instrument(symbol)
                        .encounter(act.getOpenTime())
                        .executionTime(LocalDateTime.now())
                        .valid(true)
                        .price(act.getClose()) //cambiar por el precio actual
                        .increment( (act.getVolume() - ant.getVolume()) / ant.getVolume() * 100 )
                        .build();

        }
        return alert;
    }

    private List<KlinesRequestDto> findKlines() {
            List<KlinesRequestDto> klines = binanceService.klines(symbol, INTERVAL)
                    .stream()
                    .collect(Collectors.toList())
                    .stream()
                    .sorted(Comparator.comparing(KlinesRequestDto::getCloseTime))
                    .collect(Collectors.toList());
            return klines;
    }
}
