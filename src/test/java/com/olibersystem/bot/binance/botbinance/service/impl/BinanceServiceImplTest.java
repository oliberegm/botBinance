package com.olibersystem.bot.binance.botbinance.service.impl;

import com.olibersystem.bot.binance.botbinance.dto.request.AccountSnapshotDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.KlinesRequestDto;
import com.olibersystem.bot.binance.botbinance.model.Alert;
import com.olibersystem.bot.binance.botbinance.service.BinanceService;
import com.olibersystem.bot.binance.botbinance.service.KlintesServices;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class BinanceServiceImplTest {
    @Autowired
    BinanceService binanceService;
    @Autowired
    KlintesServices klintesServices;

    @Test
    void systemGetAll() {
        binanceService.systemGetAll();
    }

    @Test
    public void accountInfo() {
        AccountSnapshotDTO accountSnapshotDTO = binanceService.accountSnapshot();
        Assertions.assertNotNull(accountSnapshotDTO);
    }
    @Test
    public void balanceByAsset() {
        AccountSnapshotDTO.Balance balance = binanceService.balanceByAsset("USDT");
        Assertions.assertNotNull(balance);
        Assertions.assertEquals("165,13536156", balance.getFree());
        Assertions.assertEquals("ACA", balance.getAsset());
        balance = binanceService.balanceByAsset("ACA1");
        Assertions.assertNull(balance);
    }

    @Test
    public void fundingWallet() {
        binanceService.fundingWallet();
        /*AccountSnapshotDTO.Balance balance =
        Assertions.assertNotNull(balance);
        Assertions.assertEquals("165,13536156", balance.getFree());
        Assertions.assertEquals("ACA", balance.getAsset());
        balance = binanceService.balanceByAsset("ACA1");
        Assertions.assertNull(balance);*/
    }

    @Test
    public void accountData() {
        //AccountRequestDTO accountRequestDTO =  binanceService.accountData();
        Double b = binanceService.getBalanceBySymbol("USDT");
        log.info("" + b);
        log.info("" + b.compareTo(184.63536157));
        log.info("" + b.compareTo(165.13536156));
        log.info("" + b.compareTo(125.13536156));

    }

    @Test
    public void coinInfo() {
        binanceService.exchangeInfo();
    }

    @Test
    public void priceInfo() {
        binanceService.pricesInfo();
    }

    @Test
    public void ordenOpen() {
        Assertions.assertTrue(binanceService.ordenOpen("BNBUSDT"));
        Assertions.assertFalse(binanceService.ordenOpen("BTCUSDT"));
    }

    @Test
    public void AveragePrice() throws InterruptedException {
        //binanceService.averagePrice("BTCUSDT");
        // convertir la lista en un objeto
        // correrla cada 1seg para ver que hace
        for(int i = 0; i < 6; i++) {
            List<KlinesRequestDto> res = binanceService.klines("MTLUSDT", "15m");
            klintesServices.add("MTLUSDT", res);
            klintesServices.alerts()
                    .forEach(f -> log.info(f.toString()));
            Thread.sleep(3000);
            /*for (KlinesRequestDto dto : res) {
                klintesServices.add("MTLUSDT", dto);
                for(Alert a : klintesServices.alerts()) {
                    log.info(a.toString());
                }
            }*/
        }



/*

        AtomicReference<Double> ant = new AtomicReference<>((double) 1);
        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger cc = new AtomicInteger(0);
        res.stream().forEach(f -> {
            //log.info(String.valueOf(LocalDateTime.ofEpochSecond( Long.valueOf(f.get(0).toString())/ 1000,0, ZoneOffset.of("-03:00"))));
            //log.info(String.valueOf(LocalDateTime.ofEpochSecond( Long.valueOf(f.get(6).toString())/ 1000,0, ZoneOffset.of("-03:00"))));
            //log.info(String.valueOf(f.get(1)));
            //log.info("===========");
            /*String fecha = String.valueOf(LocalDateTime.ofEpochSecond( Long.valueOf(f.get(0).toString())/ 1000,0, ZoneOffset.UTC));
            double prom = Double.valueOf( String.valueOf( f.get(1) )) +Double.valueOf( String.valueOf( f.get(2) ))+Double.valueOf( String.valueOf( f.get(3) ))+Double.valueOf( String.valueOf( f.get(4) ));  // ((Double) f.get(1)) + ((Double) f.get(2)) + ((Double) f.get(3)) +((Double) f.get(4));
            prom = prom / 4;
            prom = Double.valueOf( String.valueOf( f.get(1) ));
            double diff = ( prom - ant.get()) / ant.get() * 100;+
            cc.set( diff > 0 ? cc.get() + 1 : 0 );
            //System.out.println(String.format("%s, %s, %s, %s, %s ",
            //        fecha, String.valueOf( f.get(5) ), String.valueOf( f.get(7) ),  String.valueOf( f.get(9) ), String.valueOf( f.get(10) )));
            System.out.println(fecha+","+f.get(1)+","+f.get(2)+","+f.get(3)+","+f.get(4)+","+f.get(5)+","+f.get(6)+","+f.get(7)+","+f.get(8)+","+f.get(9)+","+f.get(10)+","+f.get(11)+",");
            //if(diff> 2d)
            /*System.out.println(String.format("iter: %s sec: %s  Fecha: %s ant: %s  act: %s  porc: %s ",
                    i.get(), cc.get(), fecha, ant.get(), prom,  diff));*/
            //ant.set(prom);
           // log.info(f.toString());
           // i.incrementAndGet();
        //});

    }
}