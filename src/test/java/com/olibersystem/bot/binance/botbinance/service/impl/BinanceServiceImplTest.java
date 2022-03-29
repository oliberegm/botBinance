package com.olibersystem.bot.binance.botbinance.service.impl;

import com.olibersystem.bot.binance.botbinance.dto.request.AccountRequestDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.AccountSnapshotDTO;
import com.olibersystem.bot.binance.botbinance.service.BinanceService;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class BinanceServiceImplTest {
    @Autowired
    BinanceService binanceService;

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
        ArrayList<ArrayList<Object>> res = binanceService.klines("BTCUSDT", "1m");
        ArrayList<ArrayList<Object>> res2 = res;

        ;
        for(int i = 0; i < res.size() -2; i++ ) {
            ArrayList<Object> ant = res.get(i);
            ArrayList<Object> act = res.get(i+1);
            System.out.println(i+1 + " "
                    Double.valueOf((Double) ant.get(0)).compareTo(Double.valueOf((Double) ant.get(0))) == 1 ? "subio" : "bajo"
                    +
            );
        }

        /*
        res.stream().forEach(f -> {
            log.info(String.valueOf(LocalDateTime.ofEpochSecond( Long.valueOf(f.get(0).toString())/ 1000,0, ZoneOffset.of("-03:00"))));
            log.info(String.valueOf(LocalDateTime.ofEpochSecond( Long.valueOf(f.get(6).toString())/ 1000,0, ZoneOffset.of("-03:00"))));
            log.info(String.valueOf(f.get(1)));
            log.info("===========");
            System.out.println(f.get(0)+","+f.get(1)+","+f.get(2)+","+f.get(3)+","+f.get(4)+","+f.get(5)+","+f.get(6)+","+f.get(7)+","+f.get(8)+","+f.get(9)+","+f.get(10)+","+f.get(11)+",");
        });
*/

    }
}