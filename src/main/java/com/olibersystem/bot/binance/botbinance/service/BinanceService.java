package com.olibersystem.bot.binance.botbinance.service;

import com.olibersystem.bot.binance.botbinance.dto.request.AccountRequestDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.AccountSnapshotDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.BookTickerDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.Info24HDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.KlinesRequestDto;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public interface BinanceService {

    AccountRequestDTO accountData();

    Double getBalanceBySymbol(String symbol);

    void fundingWallet();

    void systemGetAll();

    AccountSnapshotDTO accountSnapshot();

    AccountSnapshotDTO.Balance balanceByAsset(String asset);

    ExchangeInfoDTO exchangeInfo();

    List<TickerPriceDto> pricesInfo();

    List<Info24HDTO> Info24H();

    boolean newOrderTest(String instrument, String operation, double price, double quantity);

    int newOrder(String instrument, String operation, double price, double quantity);

    boolean ordenOpen(String instrument);

    String averagePrice(String symbol);

    List<KlinesRequestDto> klines(String symbol, String interval);

    List<BookTickerDTO> bookTicker();
}
