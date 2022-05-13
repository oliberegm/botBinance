package com.olibersystem.bot.binance.botbinance.service;

import com.olibersystem.bot.binance.botbinance.model.Alert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "app.scheduling.enable=false")
@Slf4j
class KlintesServicesTest {
    @Autowired
    BinanceService binanceService;
    @Autowired
    MotorDataService motorDataService;
    @Autowired
    KlintesServices klintesServices;

    @Test
    void executeOperations() {
        List<Alert> list = new ArrayList<>();
        list.add(Alert.builder()
                        .price(0.007668)
                        .instrument("MFTUSDT")
                .build());
        double t = 13041;
        binanceService.newOrder(
                "MFTUSDT",
                "BUY",
                0.007668,
                t);
    }
}