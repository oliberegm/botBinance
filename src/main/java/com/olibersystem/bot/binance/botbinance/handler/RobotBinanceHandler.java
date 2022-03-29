package com.olibersystem.bot.binance.botbinance.handler;

import com.olibersystem.bot.binance.botbinance.dto.ErrorDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.response.ExchangeInfoResponseDTO;
import com.olibersystem.bot.binance.botbinance.dto.response.TicketPriceBinanceResponseDTO;
import com.olibersystem.bot.binance.botbinance.service.BinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RobotBinanceHandler {
    private final BinanceService binanceService;

    public RobotBinanceHandler(BinanceService binanceService) {
        this.binanceService = binanceService;
    }

    public Mono<ServerResponse> allInstrument(ServerRequest request) {
        final UUID id = UUID.randomUUID();
        final ExchangeInfoResponseDTO exchangeInfo = new ExchangeInfoResponseDTO( binanceService.exchangeInfo(), null);
        return ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(exchangeInfo))
                        .onErrorResume(e -> Mono.just(e.getMessage())
                        .flatMap(f -> ServerResponse
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ExchangeInfoResponseDTO(null, new ErrorDTO(id, "Error interno: "+ f)))));
    }

    public Mono<ServerResponse> getPrices(ServerRequest request) {
        final UUID id = UUID.randomUUID();
        final TicketPriceBinanceResponseDTO ticket = new TicketPriceBinanceResponseDTO(binanceService.pricesInfo(), null);
        return ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ticket))
                        .onErrorResume(e -> Mono.just(e.getMessage())
                        .flatMap(f -> ServerResponse
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ExchangeInfoResponseDTO(null, new ErrorDTO(id, "Error Interno" + f)))));
    }
}
