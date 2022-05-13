package com.olibersystem.bot.binance.botbinance.service.impl;

import com.olibersystem.bot.binance.botbinance.model.DetailsOperation;
import com.olibersystem.bot.binance.botbinance.model.InfoBO;
import com.olibersystem.bot.binance.botbinance.model.TraderBO;
import com.olibersystem.bot.binance.botbinance.model.Transactions;
import com.olibersystem.bot.binance.botbinance.service.BinanceService;
import com.olibersystem.bot.binance.botbinance.service.BinariesService;
import com.olibersystem.bot.binance.botbinance.service.MotorDataService;
import com.olibersystem.bot.binance.botbinance.service.PriceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BinariesServiceImpl implements BinariesService {
    private static final int MAX_OPERATION = 1;
    private final PriceDataService priceDataService;
    private final MotorDataService motorDataService;
    private final BinanceService binanceService;
    private static final String USDT = "USDT";
    private double mountOperation;
    private int countOperation;

    public BinariesServiceImpl(PriceDataService priceDataService, MotorDataService motorDataService,
                               BinanceService binanceService) {
        this.priceDataService = priceDataService;
        this.motorDataService = motorDataService;
        this.binanceService = binanceService;
        mountOperation = 50D;
        countOperation = 0;
    }

    @Override
    public void generateMoney() {
        if(maxOperation()) {
            List<Transactions> transactions = findOperation();
            show(transactions);
            //executeTransaction(transactions);
        }
    }

    private void show(List<Transactions> transactions) {
        if(transactions.isEmpty()) log.info("NO ENCONTRADAS");
        transactions.forEach(f->log.info("ENCONTRADAS: "+ f.toString()));
    }

    private void executeTransaction(List<Transactions> transactions) {
        if(!transactions.isEmpty()) {
            countOperation++;
            Transactions t = transactions.get(0);
            ejecutionWait(t.getOp1());
            ejecutionWait(t.getOp2());
            ejecutionWait(t.getOp3());
            countOperation --;
        }
    }

    private boolean ejecutionWait(DetailsOperation op1) {
        if (binanceService.newOrder(
                op1.getInstrument(),
                op1.getOperation(),
                op1.getPrice(),
                op1.getCant()) != 1) {
            log.error("NO OPERATION "+op1.getInstrument());
            return false;
        }
        validateOrderClose(op1.getInstrument());
        return true;
    }

    private void validateOrderClose(String instrument) {
        while(binanceService.ordenOpen(instrument)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
            log.info("NO SE HA CERRADO " + instrument);
        }
        log.info("CERRADA " + instrument);
    }
    private List<Transactions> findOperation() {
        List<TraderBO> traderBOS = this.priceDataService.getTrading();
        //  primera opcion
        List<Transactions> result = new ArrayList<>();
        log.info("items loads: "+traderBOS.size());

        for(TraderBO op1 : traderBOS) {
            if (USDT.equals(op1.getQuote()) && assessVolume(op1.getInstrument())) {
                for (TraderBO op2: traderBOS) {
                    if (op1.getBase().equals(op2.getBase()) && !op1.getInstrument().equals(op2.getInstrument())
                            && assessVolume(op2.getInstrument())) {
                        for (TraderBO op3: traderBOS) {
                            if (op3.getInstrument().equals(op2.getQuote()+USDT) && assessVolume(op3.getInstrument())) {
                                double cant1 = op1.round(mountOperation / op1.getBuy(), 1);
                                double cant2 = op2.round(cant1 * op2.getSell(), 1);
                                double cant3 = op3.round(cant2 * op3.getSell(), 1);
                                result.add(Transactions.builder()
                                        .op1(DetailsOperation.builder()
                                                .instrument(op1.getInstrument())
                                                .operation("BUY")
                                                .symbol(op1.getBase())
                                                .cant(cant1)
                                                .price(op1.getBuy())
                                                .build())
                                        .op2(DetailsOperation.builder()
                                                .instrument(op2.getInstrument())
                                                .operation("SELL")
                                                .symbol(op2.getQuote())
                                                .cant(cant1)
                                                .price(op2.getSell())
                                                .build())
                                        .op3(DetailsOperation.builder()
                                                .instrument(op3.getInstrument())
                                                .operation("SELL")
                                                .symbol(op3.getBase())
                                                .cant(cant2)
                                                .price(op3.getSell())
                                                .build())
                                        .free(cant3)
                                        .build());
                            }
                        }
                    }
                }
            }
        }
        // fin de los calculos
        // se ordena por los mayores y los que pasen el filtro que me interesa
        return result.stream()
                .filter( fil -> fil.getFree() > calculateMountOperation())
                .sorted(Comparator.comparingDouble(Transactions::getFree).reversed())
                .collect(Collectors.toList());
    }

    private boolean maxOperation() {
        return countOperation <= MAX_OPERATION;
    }

    private double calculateMountOperation() {
        return mountOperation + mountOperation * 0.8 / 100;
        //return 90D;
    }

    private boolean assessVolume(String symbol) {
        InfoBO infoBO = motorDataService.getInfo(symbol);
        if (infoBO == null || infoBO.getInfo24HDTO() == null) {
            return true;
        }
        return Double.valueOf(infoBO.getInfo24HDTO().getVolume()) > 5000000D;
    }

}
