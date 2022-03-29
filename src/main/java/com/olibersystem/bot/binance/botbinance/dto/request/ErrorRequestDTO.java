package com.olibersystem.bot.binance.botbinance.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Builder
public class ErrorRequestDTO {
    private int code;
    private String msg;
}
