package com.olibersystem.bot.binance.botbinance.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;

@Jacksonized
@Getter
@Builder
public class AccountSnapshotDTO {
    private int code;
    private String msg;
    private ArrayList<SnapshotVo> snapshotVos;

    @Jacksonized
    @Getter
    @Builder
    public static class Balance{
        private String asset;
        private String free;
        private String locked;
    }
    @Jacksonized
    @Getter
    @Builder
    public static class Data{
        private String totalAssetOfBtc;
        private ArrayList<Balance> balances;
    }
    @Jacksonized
    @Getter
    @Builder
    public static class SnapshotVo{
        public String type;
        public Object updateTime;
        public Data data;
    }
}
