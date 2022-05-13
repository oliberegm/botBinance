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

    private final long MIN_1  = 1000 * 60;
    private final long MIN_2  = 1000 * 60 * 5;
    private final long MIN_5  = 1000 * 60 * 5;
    private final long MIN_10 = 1000 * 60 * 10;


    public BinanceTask(InstrumentService instrumentService, TasksService tasksService, KlintesServices klintesServices) {
        this.instrumentService = instrumentService;
        this.tasksService = tasksService;
        this.klintesServices = klintesServices;
    }

    @Async(TaskConfig.BINANCE_TASK_EXECUTOR)
    @Scheduled(fixedRate = MIN_1 )
    public void loadExchangeInfo() {
        log.error("*******************************************************************");
        log.error("*********************LOAD DATA*************************************");
        log.error("*******************************************************************");
        tasksService.loadData();
        log.error("*********************END LOAD DATA*********************************");
        //tasksService.binariesProcess();
    }

    @Async(TaskConfig.BINANCE_TASK_EXECUTOR)
    @Scheduled(fixedRate = MIN_1)
    public void volume() {
        log.error("*******************************************************************");
        log.error("*********************INIT BINARIES*********************************");
        log.error("*******************************************************************");
        tasksService.klintesProcess();
        log.error("*********************END INIT BINARIES*****************************");
    }

    //@Async(TaskConfig.BINANCE_TASK_EXECUTOR)
    //@Scheduled(fixedRate = MIN_1)
    public void binaries() throws InterruptedException {
        log.warn("*******************************************************************");
        log.warn("*********************INIT BINARIES*********************************");
        log.warn("*******************************************************************");
        tasksService.binariesProcess();
        log.warn("*********************END INIT BINARIES*****************************");
    }

}
