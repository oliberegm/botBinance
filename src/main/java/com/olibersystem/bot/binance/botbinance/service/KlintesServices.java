package com.olibersystem.bot.binance.botbinance.service;

import com.olibersystem.bot.binance.botbinance.dto.request.KlinesRequestDto;
import com.olibersystem.bot.binance.botbinance.model.Alert;
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
    private Map<String , Map<LocalDateTime, KlinesRequestDto>> dataStore;
    private int sizeOperation;

    public KlintesServices() {
        dataStore = new HashMap<>();
        this.sizeOperation = 5;
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
                if ( act.getVolume() > ant.getVolume() * 5 ) {
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
                .forEach(f -> log.warn("ENCONTRADO: "+f.toString()));
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

}
