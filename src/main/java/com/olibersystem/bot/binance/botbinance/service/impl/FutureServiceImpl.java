package com.olibersystem.bot.binance.botbinance.service.impl;

import com.olibersystem.bot.binance.botbinance.service.FuturesService;
import com.olibersystem.bot.binance.botbinance.service.MotorDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FutureServiceImpl implements FuturesService {
    private final MotorDataService motorDataService;

    public FutureServiceImpl(MotorDataService motorDataService) {
        this.motorDataService = motorDataService;
    }

    public void findAlternative() {
        log.info("===========Buscando Alternativas==========");
        //motorDataService.



    }






}
