package com.olibersystem.bot.binance.botbinance.service;

import com.olibersystem.bot.binance.botbinance.model.Transactions;

import java.util.List;

public interface InstrumentService {
    List<String> getSymbols();

    List<Transactions> findOperation(String symbol, Double amountInit);

    boolean executeOperation(Transactions transactions, String symbol);

    void executeThreeOperation();
}
