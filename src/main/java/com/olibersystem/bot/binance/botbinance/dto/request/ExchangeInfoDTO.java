package com.olibersystem.bot.binance.botbinance.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Jacksonized
@Getter
@Builder
public class ExchangeInfoDTO {
    private  String timezone;
    private  long serverTime;
    private  List<ExchangeInfoDTO.RateLimitRequestDto> rateLimits;
    private  List<Object> exchangeFilters;
    private  List<ExchangeInfoDTO.SymbolRequestDto> symbols;

    @Jacksonized
    @Getter
    @Builder
    public static class RateLimitRequestDto {
        private  String rateLimitType;
        private  String interval;
        private  int intervalNum;
        private  int limit;
    }

    @Jacksonized
    @Getter
    @Builder
    public static class SymbolRequestDto {
        private String symbol;
        private String status;
        private String baseAsset;
        private int baseAssetPrecision;
        private String quoteAsset;
        private int quotePrecision;
        private int quoteAssetPrecision;
        private int baseCommissionPrecision;
        private int quoteCommissionPrecision;
        private ArrayList<String> orderTypes;
        private boolean icebergAllowed;
        private boolean ocoAllowed;
        private boolean quoteOrderQtyMarketAllowed;
        private boolean allowTrailingStop;
        private boolean isSpotTradingAllowed;
        private boolean isMarginTradingAllowed;
        private boolean spotTradingAllowed;
        private boolean marginTradingAllowed;
        private ArrayList<FilterRequestDto> filters;
        private ArrayList<String> permissions;
    }

    @Jacksonized
    @Getter
    @Builder
    public static class FilterRequestDto {
        private String filterType;
        private String minPrice;
        private String maxPrice;
        private String tickSize;
        private String multiplierUp;
        private String multiplierDown;
        private int avgPriceMins;
        private String minQty;
        private String maxQty;
        private String stepSize;
        private String minNotional;
        private boolean applyToMarket;
        private int limit;
        private int maxNumOrders;
        private int maxNumAlgoOrders;
        private String maxPosition;
    }
}



