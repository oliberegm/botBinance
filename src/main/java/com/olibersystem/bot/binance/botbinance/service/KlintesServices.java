package com.olibersystem.bot.binance.botbinance.service;

import com.olibersystem.bot.binance.botbinance.dto.request.KlinesRequestDto;
import com.olibersystem.bot.binance.botbinance.model.Alert;
import com.olibersystem.bot.binance.botbinance.model.InfoBO;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KlintesServices {
    private static final int MAX_OPERATION = 1;
    private static final String INTERVAL = "5m";
    private static final double SIZE_VOLUME = 5D;
    private Map<String , Map<LocalDateTime, KlinesRequestDto>> dataStore;
    private final BinanceService binanceService;
    private final MotorDataService motorDataService;

    private int sizeOperation;
    private int countOperation;

    public KlintesServices(BinanceService binanceService, MotorDataService motorDataService) {
        this.binanceService = binanceService;
        this.motorDataService = motorDataService;
        dataStore = new HashMap<>();
        this.sizeOperation = 5;
        this.countOperation = 0;
    }

    public void setSizeOperation(int size) {
        sizeOperation = size;
    }

    public void add(String instrument, List<KlinesRequestDto> data) {
        if(data != null) {
            data.forEach(f -> this.add(instrument, f));
        }
    }

    public void add(String instrument, KlinesRequestDto data) {
        Map<LocalDateTime, KlinesRequestDto> local = dataStore.get(instrument);
        if ( local == null ) {
            local = new HashMap<>();
        }
        local.put(data.getOpenTime(), data);
        dataStore.put(instrument, local);
    }

    public List<Alert> alerts() {
        List<Alert> alertList = new ArrayList<>();
        for(Map.Entry<String, Map<LocalDateTime, KlinesRequestDto>> instrument: dataStore.entrySet()) {
            List<KlinesRequestDto> klines = instrument.getValue().values()
                    .stream()
                    .collect(Collectors.toList())
                        .stream()
                        .sorted(Comparator.comparing(KlinesRequestDto::getCloseTime))
                        .collect(Collectors.toList());
            KlinesRequestDto ant = klines.get(klines.size() - 2);
            KlinesRequestDto act = klines.get(klines.size() - 1);
            //log.info("busca: "+act.getCloseTime()+" "+act.getVolume() +" "+ ant.getVolume() +" "+ ant.getVolume() * 10);
            if (ant.getOpen() < ant.getClose() && act.getOpen() < act.getClose() && act.getVolume()+ant.getVolume() > 300000d) {
                if ( act.getVolume() > ant.getVolume() * SIZE_VOLUME ) {
                    alertList.add(Alert.builder()
                                    .instrument(instrument.getKey())
                                    .encounter(act.getOpenTime())
                                    .executionTime(LocalDateTime.now())
                                    .valid(true)
                                    .price(act.getClose()) //cambiar por el precio actual
                                    .increment( (act.getVolume() - ant.getVolume()) / ant.getVolume() * 100 )
                                    .build());
                    //log.info("busca: "+act.getCloseTime()+" "+act.getVolume() +" "+ ant.getVolume() +" "+ ant.getVolume() * 10);
                    // calculo el porcentaje de incremento
                }
            }
        }
        return alertList;
    }

    public void printAlert() {
        this.alerts()
                .forEach(f -> System.out.println("ENCONTRADO: "+f.toString()));
    }
    public void printAlert(String instrument){
        this.alerts().stream()
                .filter(fil -> instrument.equals( fil.getInstrument()))
                .forEach(f -> System.out.println("ENCONTRADO: "+f.toString()));
    }

    private Alert evaluateOperation(Map.Entry<String, List<KlinesRequestDto>> klinesRequestDtoList) {
        Alert alert = new Alert();
        alert.setValid(false);
        if ( klinesRequestDtoList.getValue().size() > 2) {
            KlinesRequestDto ant = klinesRequestDtoList.getValue().get(klinesRequestDtoList.getValue().size() - 2);
            KlinesRequestDto act = klinesRequestDtoList.getValue().get(klinesRequestDtoList.getValue().size() - 1);
            //log.info("busca: "+act.getCloseTime()+" "+act.getVolume() +" "+ ant.getVolume() +" "+ ant.getVolume() * 10);
            if (ant.getOpen() < ant.getClose() && act.getOpen() < act.getClose() && act.getVolume()+ant.getVolume() > 300000d) {
                if ( act.getVolume() > ant.getVolume() * 10 ) {
                    //log.info("busca: "+act.getCloseTime()+" "+act.getVolume() +" "+ ant.getVolume() +" "+ ant.getVolume() * 10);
                    // calculo el porcentaje de incremento
                    alert.setIncrement( (act.getVolume() - ant.getVolume()) / ant.getVolume() * 100 );
                    alert.setValid(true);
                    alert.setExecutionTime(LocalDateTime.now());
                    alert.setEncounter(act.getCloseTime());
                    alert.setInstrument(klinesRequestDtoList.getKey());
                }
            }
        }
        return alert;
    }

    private LocalDateTime getTime(String time) {
        return LocalDateTime.ofEpochSecond( Long.valueOf(time)/ 1000,0, ZoneOffset.UTC);
    }
    private String getTimeString(String time) {
        return String.valueOf(getTime(time));
    }

    public void executeOperations(){
        Optional<Alert> al =  alerts().stream()
                .findFirst();
        if(al.isPresent()) {
            double quantity = motorDataService.getInstrumentByInstrument(al.get().getInstrument()).evaluateAmount( 50D / al.get().getPrice());
            if(1 != binanceService.newOrder(
                    al.get().getInstrument(),
                    "BUY",
                    al.get().getPrice(),
                    quantity)) {
                return;
            }
            try {
                validateOrderClose(al.get().getInstrument());
            } catch (Exception e ) {}
            binanceService.newOrder(
                    al.get().getInstrument(),
                    "SELL",
                    al.get().getPrice(),
                    quantity
            );
        }

    }

    private boolean maxOperation() {
        return countOperation <= MAX_OPERATION;
    }

    private void validateOrderClose(String instrument) throws InterruptedException {
        while(binanceService.ordenOpen(instrument)) {
            Thread.sleep(500);
            log.info("NO SE HA CERRADO " + instrument);
        }
        log.info("CERRADA " + instrument);
    }

    public void generateMoney() {

        if(maxOperation()) {
            // todo creo los hilos para los otros otros procesos de los symbolos por independientes
            motorDataService.getTraderBySymbolInstrument()
                    .forEach( dto ->{
                        (new AnalisisVolumenService(dto.getInstrument().getSymbol(), binanceService, motorDataService)).start();
                    });
        }
        //countOperation = 3;

    }

    private void findKlines(String instrument) {
        try {
            List<KlinesRequestDto> listt = binanceService.klines(instrument, INTERVAL);
            add(instrument, listt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean assessVolume(String symbol) {
        InfoBO infoBO = motorDataService.getInfo(symbol);
        if (infoBO == null || infoBO.getInfo24HDTO() == null) {
            return true;
        }
        return Double.valueOf(infoBO.getInfo24HDTO().getVolume()) > 500000D;
    }
}
