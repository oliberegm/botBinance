package com.olibersystem.bot.binance.botbinance.service.impl;

import com.olibersystem.bot.binance.botbinance.model.DetailsOperation;
import com.olibersystem.bot.binance.botbinance.model.InstrumentBO;
import com.olibersystem.bot.binance.botbinance.model.Transactions;
import com.olibersystem.bot.binance.botbinance.service.BinanceService;
import com.olibersystem.bot.binance.botbinance.service.InstrumentService;
import com.olibersystem.bot.binance.botbinance.service.MotorDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InstrumentServiceImpl implements InstrumentService {
    private final int MAXLEVEL = 0;
    private final BinanceService binanceService;
    private final MotorDataService motorDataService;
    private long seg;
    private long positives;

    public InstrumentServiceImpl(BinanceService binanceService, MotorDataService motorDataService) {
        this.binanceService = binanceService;
        this.motorDataService = motorDataService;
        seg = 0L;
        positives = 0l;
    }




    @Override
    public List<String> getSymbols() {
        return motorDataService.getAllNames();
    }

    @Override
    public List<Transactions> findOperation(String symbol, Double amountInit) {
        List<Transactions> result = new ArrayList<>();
        log.info("items loads: "+motorDataService.getAllInstrument().size());
        List<InstrumentBO> traderBOS = motorDataService.getTraderBySymbolInstrument(symbol);
        traderBOS.stream().forEach(f-> {
            double p1 = f.amountOperation(symbol, amountInit);
            String s1 = f.getSymbol(symbol);
            String i1 = f.getInstrument().getSymbol();
            String o1 = f.typeOperation(symbol);
            double d1 = f.getPrice();
            //System.out.println(String.format("USDT %s %s ", s1, p1));
            motorDataService.getTraderBySymbolInstrumentNoUSDT(s1).stream().forEach(sub -> {
                double p2 = sub.amountOperation(s1, p1);
                String s2 = sub.getSymbol(s1);
                String i2 = sub.getInstrument().getSymbol();
                String o2 = sub.typeOperation(s1);
                double d2 = sub.getPrice();
                //System.out.println(String.format("USDT %s %s %s %s", s1, s2, p1, p2));
                motorDataService.getTraderBySymbolInstrument(s2, symbol).stream().forEach(end-> {
                    double p3 = end.amountOperation(s2, p2);
                    String s3 = end.getSymbol(s2);
                    String i3 = end.getInstrument().getSymbol();
                    String o3 = end.typeOperation(s2);
                    double d3 = end.getPrice();
                    //System.out.println(String.format("USDT %s %s %s %s %s %s %s", s1, s2, s3, p1, p2, p3, p1*p2*p3));
                    if ( p3 > (amountInit + amountInit * 0.8 / 100) ) {
                        result.add(
                                Transactions.builder()
                                        .op1(DetailsOperation.builder().instrument(i1).operation(o1).cant(p1).symbol(s1).price(d1).build())
                                        .op2(DetailsOperation.builder().instrument(i2).operation(o2).cant(p2).symbol(s2).price(d2).build())
                                        .op3(DetailsOperation.builder().instrument(i3).operation(o3).cant(p3).symbol(s3).price(d3).build())
                                        .free(p3)
                                        .build());
                                //new Transactions(f, sub, end, p1*p2*p3 ));
                       log.debug(String.format("O1[%s %s %s]        O2[%s %s %s]          O3[%s %s %s]",
                               i1, o1, p1, i2, o2, p2, i3, o3, p3));
                    }
                });
            });
        });
        log.info(String.format("OPERACIONES: %s", result.size()));
        List<Transactions> result2 = result.stream().sorted(Comparator.comparingDouble(Transactions::getFree).reversed()).collect(Collectors.toList());
        result2.stream().forEach(f->log.info(f.toString()));
        return result2;
    }

    @Override
    public boolean executeOperation(Transactions transactions, String symbol) {
        if (binanceService.newOrder(
                transactions.getOp1().getInstrument(),
                transactions.getOp1().getOperation(),
                transactions.getOp1().getPrice(),
                transactions.getOp1().getCant()) != 1) {
            log.error("NO OPERATION 1");
            return false;
        }
        this.validateOrderClose(transactions.getOp1().getInstrument());
        if (binanceService.newOrder(
                        transactions.getOp2().getInstrument(),
                        transactions.getOp2().getOperation(),
                        transactions.getOp2().getPrice(),
                        transactions.getOp1().getCant()) != 1) {
            log.error("NO OPERATION 2");
            return false;
        }
            this.validateOrderClose(transactions.getOp2().getInstrument());
        if (binanceService.newOrder(
                        transactions.getOp3().getInstrument(),
                        transactions.getOp3().getOperation(),
                        transactions.getOp3().getPrice(),
                        transactions.getOp2().getCant()) != 1) {
            log.error("NO OPERATION 2");
            return false;
            }
            return true;
    }

    @Override
    public void executeThreeOperation() {
        log.error("=============Init Load price==============" + seg++);
        Double amount = 100D;
        List<Transactions>  transactions = findOperation("USDT", amount);
                /*.stream()
                .filter(f-> !f.getOp1().getSymbol().equals("BTC") &&
                        !f.getOp2().getSymbol().equals("BTC") &&
                        !f.getOp3().getSymbol().equals("BTC"))
                .filter(f-> !f.getOp1().getSymbol().equals("EUR") &&
                        !f.getOp2().getSymbol().equals("EUR") &&
                        !f.getOp3().getSymbol().equals("EUR"))
                .filter(f-> !f.getOp1().getSymbol().equals("GBP") &&
                        !f.getOp2().getSymbol().equals("GBP") &&
                        !f.getOp3().getSymbol().equals("GBP"))
                .collect(Collectors.toList());*/
        log.error("CANT: "+transactions.size());
        transactions.forEach(f -> log.info(f.toString()));

        if (!transactions.isEmpty() ) {
            executeOperation(transactions.get(0), "USDT");
            positives ++;
        }
        log.error("==============END======================> "+positives);
    }

    private void validateOrderClose(String instrument) {
        while(binanceService.ordenOpen(instrument)) {
            //Thread.sleep(500);
            log.error("NO SE HA CERRADO " + instrument);
        }
        log.error("CERRADA " + instrument);
    }

    private Double validateFound(String instrument, Double amount) {
        Double a = 0.0;
        //a = binanceService.getBalanceBySymbol(instrument);
        a = motorDataService.getInstrumentByInstrument(instrument).evaluateAmount(amount -
                motorDataService.getInstrumentByInstrument(instrument).discount());
        log.error("HAY FONDOS "+instrument+ " HAY "+a+" === NECESITO: "+amount);
        return a;
    }

    private Double validateRound(String instrument, Double amount) {
        Double a = 0.0;
        //a = binanceService.getBalanceBySymbol(instrument);
        a = motorDataService.getInstrumentByInstrument(instrument).evaluateAmount(amount);
        log.error("REDONDEO FONDOS "+instrument+ " HAY "+a+" === NECESITO: "+amount);
        return a;
    }

    private boolean validateTest(Transactions transactions) {
        return binanceService.newOrderTest(
                transactions.getOp1().getInstrument(),
                transactions.getOp1().getOperation(),
                transactions.getOp1().getCant(),
                transactions.getOp1().getCant()
        ) && binanceService.newOrderTest(
                transactions.getOp2().getInstrument(),
                transactions.getOp2().getOperation(),
                transactions.getOp2().getCant(),
                transactions.getOp2().getCant()
        ) && binanceService.newOrderTest(
                transactions.getOp3().getInstrument(),
                transactions.getOp3().getOperation(),
                transactions.getOp3().getCant(),
                transactions.getOp3().getCant()
        );
    }

    public class Vars{
        public String symbol;
        public Double amount;
    }

}
