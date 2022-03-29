package com.olibersystem.bot.binance.botbinance.exceptions;

import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.scheduler.Schedulers;

public class LogTrace {
    public static void logTraceResponse(Logger log, ClientResponse response) {
        if (log.isTraceEnabled()) {
            log.trace("Response status: {}", response.statusCode());
            log.trace("Response headers: {}", response.headers().asHttpHeaders());
            response.bodyToMono(String.class)
                    .publishOn(Schedulers.boundedElastic())
                    .subscribe(body -> log.trace("Response body: {}", body));
        }
    }
}
