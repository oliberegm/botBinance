package com.olibersystem.bot.binance.botbinance.router;

import com.olibersystem.bot.binance.botbinance.DataBasic;
import com.olibersystem.bot.binance.botbinance.dto.request.ExchangeInfoDTO;
import com.olibersystem.bot.binance.botbinance.dto.request.TickerPriceDto;
import com.olibersystem.bot.binance.botbinance.dto.response.ExchangeInfoResponseDTO;
import com.olibersystem.bot.binance.botbinance.dto.response.TicketPriceBinanceResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RobotBinanceRouterTest {
    @Autowired
    private WebTestClient webTestClient;

    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    private WebClient.RequestHeadersSpec requestHeadersMock;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.RequestBodySpec requestBodyMock;
    private WebClient.ResponseSpec responseMock;
    @MockBean
    private WebClient webClientMock;

    @BeforeEach
    void mockWebClient() {
        requestBodyUriMock = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        requestBodyMock = Mockito.mock(WebClient.RequestBodySpec.class);
        responseMock = Mockito.mock(WebClient.ResponseSpec.class);
        webTestClient = webTestClient.mutate()
                .responseTimeout(Duration.ofMillis(30000))
                .build();
    }

    @Test
    void infoAllOk() {
        final String urlInfoAll = "infoBinance/infoAll";
        Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersMock);
        Mockito.when(requestHeadersMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersMock);
        Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        Mockito.when(responseMock.bodyToMono(ExchangeInfoDTO.class)).thenReturn(Mono.just(DataBasic.basicExchangeInfoDto()));

        webTestClient
                .get().uri(urlInfoAll)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExchangeInfoResponseDTO.class).value(info -> {
                    assertThat(info).isNotNull();
                    assertThat(info.getErrorDTO()).isNull();
                    assertThat(info.getData()).isNotNull();
                });
    }


    //@Test
    void infoAllErro() {
        final String urlInfoAll = "infoBinance/infoAll";
        Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersMock);
        Mockito.when(requestHeadersMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersMock);
        //responseMock = new WebClient.ResponseSpec();
        Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        Mockito.when(responseMock.onStatus(Mockito.any(Predicate.class), Mockito.any(Function.class)))
                        .thenReturn(responseMock.onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new Exception("500 error!"))));
        Mockito.when(responseMock.bodyToMono(ExchangeInfoDTO.class)).thenReturn(Mono.just(DataBasic.basicExchangeInfoDto()));
        webTestClient
                .get().uri(urlInfoAll)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ExchangeInfoResponseDTO.class).value(info -> {
                    assertThat(info).isNotNull();
                    assertThat(info.getErrorDTO()).isNotNull();
                    assertThat(info.getErrorDTO()).isNotNull();
                    assertThat(info.getData()).isNull();
                });
    }

    @Test
    public void getPricesBinanceOkTest(){
        final String url = "infoBinance/pricesBinance";
        Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersMock);
        Mockito.when(requestHeadersMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersMock);
        Mockito.when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        Mockito.when(responseMock.bodyToMono(new ParameterizedTypeReference<List<TickerPriceDto>>() {})).thenReturn(Mono.just(DataBasic.tickerPriceDto()));

        webTestClient
                .get().uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketPriceBinanceResponseDTO.class).value(info -> {
                    assertThat(info).isNotNull();
                    assertThat(info.getErrorDTO()).isNull();
                    assertThat(info.getData()).isNotNull();
                });
    }

}