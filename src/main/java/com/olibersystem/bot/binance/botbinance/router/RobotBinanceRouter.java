package com.olibersystem.bot.binance.botbinance.router;

import com.olibersystem.bot.binance.botbinance.handler.RobotBinanceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class RobotBinanceRouter {
    @Bean
    public RouterFunction<ServerResponse> infoBinanceRoute(RobotBinanceHandler infoBinanceHandler) {
        return RouterFunctions.nest(RequestPredicates.path("/infoBinance"),
                RouterFunctions
                        .route(GET("/infoAll").and(accept(MediaType.APPLICATION_JSON)),
                            infoBinanceHandler::allInstrument)
                        .andRoute(GET("pricesBinance").and(accept(MediaType.APPLICATION_JSON)),
                            infoBinanceHandler::getPrices));

    }
}
