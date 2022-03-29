package com.olibersystem.bot.binance.botbinance.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class OrdersRequestDTO {
    private String symbol;
    private long orderId;
    private int orderListId;
    private String clientOrderId;
    private String price;
    private String origQty;
    private String executedQty;
    private String cummulativeQuoteQty;
    private String status;
    private String timeInForce;
    private String type;
    private String side;
    private String stopPrice;
    private String icebergQty;
    private long time;
    private long updateTime;
    private boolean isWorking;
    private String origQuoteOrderQty;
}
