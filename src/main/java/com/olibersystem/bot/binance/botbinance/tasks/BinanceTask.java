package com.olibersystem.bot.binance.botbinance.tasks;

import com.olibersystem.bot.binance.botbinance.config.TaskConfig;
import com.olibersystem.bot.binance.botbinance.service.InstrumentService;
import com.olibersystem.bot.binance.botbinance.service.KlintesServices;
import com.olibersystem.bot.binance.botbinance.service.TasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Slf4j
public class BinanceTask {
    private final InstrumentService instrumentService;
    private final TasksService tasksService;
    private final KlintesServices klintesServices;

    private final long SEG_FIXED = 1000;
    private final long MIN_5 = 1000 * 60 * 5;
    private final long FIXED2 = 100000 * 60 * 5;


    public BinanceTask(InstrumentService instrumentService, TasksService tasksService, KlintesServices klintesServices) {
        this.instrumentService = instrumentService;
        this.tasksService = tasksService;
        this.klintesServices = klintesServices;
    }

    @Async(TaskConfig.BINANCE_TASK_EXECUTOR)
    @Scheduled(fixedRate = MIN_5 )
    public void loadExchangeInfo() {
        log.warn("*******************************************************************");
        log.warn("*********************LOAD DATA*************************************");
        log.warn("*******************************************************************");
        tasksService.loadData();
        log.warn("*********************END LOAD DATA*********************************");
    }

    //@Async(TaskConfig.BINANCE_TASK_EXECUTOR)
    //@Scheduled(fixedRate = 25000)
    public void loadPriceInfo() throws InterruptedException {
        //instrumentService.executeThreeOperation();
    }
}
