package com.olibersystem.bot.binance.botbinance.service;

import com.olibersystem.bot.binance.botbinance.dto.request.KlinesRequestDto;
import com.olibersystem.bot.binance.botbinance.model.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KlintesServices {
    private Map<String , List<KlinesRequestDto>> dataStore;
    private int sizeOperation;

    public KlintesServices() {
        dataStore = new HashMap<>();
        this.sizeOperation = 5;
    }

    public void setSizeOperation(int size) {
        sizeOperation = size;
    }

    public void add(String instrument, KlinesRequestDto data) {
        List<KlinesRequestDto> local = dataStore.get(instrument);
        if ( local == null ) {
            local = new ArrayList();
        }
        if(local.size() == sizeOperation) {
            local.remove(0);
        }
        local.add(data);
        dataStore.put(instrument, local);
    }

    public List<Alert> alerts() {
        List<Alert> alertList =  dataStore.entrySet().stream()
                .map(map -> evaluateOperation(map) )
                .filter(fill -> fill.isValid())
                .sorted(Comparator.comparingDouble(Alert::getIncrement))
                .collect(Collectors.toList());
        return alertList;
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

}
