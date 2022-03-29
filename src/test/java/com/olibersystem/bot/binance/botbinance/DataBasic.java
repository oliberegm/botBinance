package com.olibersystem.bot.binance.botbinance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DataBasic {
    public static ExchangeInfoDTO basicExchangeInfoDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        ExchangeInfoDTO exchangeInfoRequestDto = null;
        try {
            exchangeInfoRequestDto = objectMapper.readValue(objectMapper.getClass().getClassLoader().getResource("json/infoAllBinance.json"), ExchangeInfoDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exchangeInfoRequestDto;
    }

    public static List<TickerPriceDto> tickerPriceDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TickerPriceDto> tickerPriceDto = null;
        try {
            tickerPriceDto = objectMapper.readValue(objectMapper.getClass().getClassLoader().getResource("json/pricesbinance.json"), new TypeReference<List<TickerPriceDto>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tickerPriceDto;
    }

    public static String tickerPrice() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("json/pricesbinance.json");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
    public static String exchangeInfo() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("json/infoAllBinance.json");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
}
