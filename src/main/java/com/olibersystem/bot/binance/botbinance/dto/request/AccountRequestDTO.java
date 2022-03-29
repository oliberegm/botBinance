package com.olibersystem.bot.binance.botbinance.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;

@Builder
@Data
@Jacksonized
public class AccountRequestDTO {
    private int makerCommission;
    private int takerCommission;
    private int buyerCommission;
    private int sellerCommission;
    private boolean canTrade;
    private boolean canWithdraw;
    private boolean canDeposit;
    private long updateTime;
    private String accountType;
    private ArrayList<Balance> balances;
    private ArrayList<String> permissions;

    @Builder
    @Data
    @Jacksonized
    public static class Balance{
        private String asset;
        private String free;
        private String locked;
    }
}
