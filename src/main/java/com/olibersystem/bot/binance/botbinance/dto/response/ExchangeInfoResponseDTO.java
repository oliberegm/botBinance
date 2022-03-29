package com.olibersystem.bot.binance.botbinance.dto.response;

import com.olibersystem.bot.binance.botbinance.dto.ErrorDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ExchangeInfoResponseDTO {
    private ExchangeInfoDTO data;
    private ErrorDTO errorDTO;
    private Boolean status;
    public ExchangeInfoResponseDTO(ExchangeInfoDTO data, ErrorDTO errorDTO ) {
        this.data = data;
        this.errorDTO = errorDTO;
        status = false;
        if(data == null) status = true;

    }
}
