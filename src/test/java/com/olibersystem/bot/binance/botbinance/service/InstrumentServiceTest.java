package com.olibersystem.bot.binance.botbinance.service;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import com.olibersystem.bot.binance.botbinance.DataBasic;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class InstrumentServiceTest {
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    private WebClient.RequestHeadersSpec requestHeadersMock;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.RequestBodySpec requestBodyMock;
    private WebClient.ResponseSpec responseMock;
    @MockBean
    private WebClient webClientMock;

    @MockBean
    private SpotClientImpl client;

    @Autowired
    private InstrumentService instrumentService;

    @BeforeEach
    void mockWebClient() {
        requestBodyUriMock = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        requestBodyMock = Mockito.mock(WebClient.RequestBodySpec.class);
        responseMock = Mockito.mock(WebClient.ResponseSpec.class);
    }

    @Test
    public void getSymbols() throws IOException {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        Mockito.when(client.createMarket()).thenReturn(Mockito.mock(Market.class));
        Mockito.when(client.createMarket().exchangeInfo(parameters)).thenReturn(DataBasic.exchangeInfo());

        List<String> symbolBOMap =  instrumentService.getSymbols();
        Assertions.assertFalse(symbolBOMap.isEmpty());
    }
    @Test
    public void findOperation() throws IOException {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        Mockito.when(client.createMarket()).thenReturn(Mockito.mock(Market.class));
        Mockito.when(client.createMarket().tickerSymbol(parameters)).thenReturn(DataBasic.tickerPrice());
        Mockito.when(client.createMarket().exchangeInfo(parameters)).thenReturn(DataBasic.exchangeInfo());

        instrumentService.findOperation("USDT", 100D);
    }

}
