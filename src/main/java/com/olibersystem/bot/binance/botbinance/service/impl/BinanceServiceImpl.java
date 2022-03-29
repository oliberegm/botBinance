package com.olibersystem.bot.binance.botbinance.service.impl;

import com.binance.connector.client.exceptions.BinanceClientException;
import com.binance.connector.client.exceptions.BinanceConnectorException;
import com.binance.connector.client.impl.SpotClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olibersystem.bot.binance.botbinance.dto.request.AccountRequestDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.AccountSnapshotDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.ErrorRequestDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.OrdersRequestDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import com.olibersystem.bot.binance.botbinance.service.BinanceService;
import com.olibersystem.bot.binance.botbinance.service.MotorDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BinanceServiceImpl implements BinanceService {

    private final SpotClientImpl client;
    private final ObjectMapper objectMapper;
    private final MotorDataService motorDataService;

    public BinanceServiceImpl(SpotClientImpl client, ObjectMapper objectMapper, MotorDataService motorDataService){
        this.client = client;
        this.objectMapper = objectMapper;
        this.motorDataService = motorDataService;
    }

    @Override
    public AccountRequestDTO accountData() {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        try {
            String result = client.createTrade().account(parameters);
            return objectMapper.readValue(result, AccountRequestDTO.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        } catch (BinanceConnectorException e) {
            log.error("fullErrMessage: {}", e.getMessage());
        } catch (BinanceClientException e) {
            log.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode());
        }
        return null;
    }

    @Override
    public Double getBalanceBySymbol(String symbol) {
        AccountRequestDTO accountRequestDTO = this.accountData();
        AccountRequestDTO.Balance balance = accountRequestDTO.getBalances().stream()
                .filter(f -> f.getAsset().equals(symbol))
                .findFirst()
                .orElse(null);
        if ( balance == null ) return 0.0;
        return  Double.parseDouble(balance.getFree());
    }

    @Override
    public void fundingWallet() {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        String result = client.createTrade().account(parameters);
        log.info(result);
    }

    @Override
    public void systemGetAll() {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        String result = client.createWallet().systemStatus();
        log.info(result);
    }

    @Override
    public AccountSnapshotDTO accountSnapshot() {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "SPOT");
        String result = client.createWallet().accountSnapshot(parameters);
        try {
            return objectMapper.readValue(result, AccountSnapshotDTO.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public AccountSnapshotDTO.Balance balanceByAsset(String asset) {
        AccountSnapshotDTO accountSnapshotDTO = accountSnapshot();
        AccountSnapshotDTO.SnapshotVo snapshotVo = accountSnapshotDTO.getSnapshotVos().get(accountSnapshotDTO.getSnapshotVos().size() -1 );
        return snapshotVo.data.getBalances().stream().filter( f -> f.getAsset().equals(asset)).findFirst().orElse(null);
    }

    @Override
    public ExchangeInfoDTO exchangeInfo() {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        String result = client.createMarket().exchangeInfo(parameters);
        try {
            return objectMapper.readValue(result, ExchangeInfoDTO.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<TickerPriceDto> pricesInfo() {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        String result = client.createMarket().tickerSymbol(parameters);
        try {
            return Arrays.stream(objectMapper.readValue(result, TickerPriceDto[].class))
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean newOrderTest(String instrument, String operation, double price, double quantity) {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", instrument);
        parameters.put("side", operation);
        parameters.put("type", "MARKET");
        parameters.put("quantity", quantity);
        //parameters.put("price", price);

        try {
            String result = client.createTrade().testNewOrder(parameters);
            return true;
        }
        catch (BinanceConnectorException e) {
            log.error(String.format("ERROR TEST %s %s %s", instrument, operation, quantity));
            log.error("TEST fullErrMessage: {}", e.getMessage());
        }
        catch (BinanceClientException e) {
            log.error(String.format("ERROR TEST %s %s %s", instrument, operation, quantity));
            log.error("TEST fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode());
        }
        return false;
    }
    @Override
    public int newOrder(String instrument, String operation, double price, double quantity) {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", instrument);
        parameters.put("side", operation);
        parameters.put("type", "MARKET");
        parameters.put("quantity", quantity);
        //parameters.put("price", price);

        try {
            log.info(String.format("EXE %s %s %s", instrument, operation, quantity));
            String result = client.createTrade().newOrder(parameters);
            log.info(String.format("OK %s %s %s %s", instrument, operation, quantity, result));
            return 1;
        }
        catch (BinanceConnectorException e) {
            log.error(String.format("ERROR %s %s %s", instrument, operation, quantity));
            log.error("fullErrMessage: {}", e.getMessage());
            try {
                ErrorRequestDTO errorRequestDTO = objectMapper.readValue(e.getMessage(), ErrorRequestDTO.class);
                return errorRequestDTO.getCode() == -2010 ? 2: 3;
            } catch (JsonProcessingException e1) {
                log.error(e1.getMessage());
            }

        }
        catch (BinanceClientException e) {
            log.error(String.format("ERROR %s %s %s", instrument, operation, quantity));
            log.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode());
            try {
                ErrorRequestDTO errorRequestDTO = objectMapper.readValue(e.getMessage(), ErrorRequestDTO.class);
                return errorRequestDTO.getCode() == -2010 ? 2: 3;
            } catch (JsonProcessingException e1) {
                log.error(e1.getMessage());
            }
        }
        return 0;
    }

    @Override
    public boolean ordenOpen(String instrument) {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol", instrument);
        try {
            String result = client.createTrade().getOpenOrders(parameters);
            log.info(result);
            List<OrdersRequestDTO> orders =  Arrays.stream(objectMapper.readValue(result, OrdersRequestDTO[].class))
                    .collect(Collectors.toList());
            return !orders.isEmpty();
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        catch (BinanceConnectorException e) {
            log.error("fullErrMessage: {}", e.getMessage());
        }
        catch (BinanceClientException e) {
            log.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode());
        }
        return false;
    }

    @Override
    public String averagePrice(String symbol) {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol",symbol);
        String result = client.createMarket().averagePrice(parameters);
        log.info(result);
        return result;
    }

    @Override
    public ArrayList<ArrayList<Object>> klines(String symbol, String interval) {
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();
        parameters.put("symbol",symbol);
        parameters.put("interval",interval);
        String result = client.createMarket().klines(parameters);
        log.info(result);
        try {
            return objectMapper.readValue(result, new TypeReference<ArrayList<ArrayList<Object>>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
