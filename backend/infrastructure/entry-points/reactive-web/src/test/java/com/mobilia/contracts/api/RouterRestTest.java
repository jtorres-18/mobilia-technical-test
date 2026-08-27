package com.mobilia.contracts.api;
import com.mobilia.contracts.api.handler.ContractSearchHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;


class RouterRestTest {

    private ContractSearchHandler handler;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        handler = mock(ContractSearchHandler.class);

        RouterRest routerRest =
                new RouterRest("/api/v1/contracts");

        RouterFunction<ServerResponse> routes = routerRest.contractRoutes(handler);

        webTestClient = WebTestClient.bindToRouterFunction(routes).build();
    }

    @Test
    void shouldRouteFindAllContractsRequest() {
        when(handler.findAll(any()))
                .thenReturn(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue("find-all"));

        webTestClient.get()
                .uri("/api/v1/contracts")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .isEqualTo("find-all");

        verify(handler).findAll(any());
    }

    @Test
    void shouldRouteSearchContractsRequest() {
        when(handler.search(any())).thenReturn(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("search"));

        webTestClient.get()
                .uri("/api/v1/contracts/search?q=Ana")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .isEqualTo("search");

        verify(handler).search(any());
    }

    @Test
    void shouldReturnNotFoundForUnknownRoute() {
        webTestClient.get()
                .uri("/api/v1/unknown")
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}