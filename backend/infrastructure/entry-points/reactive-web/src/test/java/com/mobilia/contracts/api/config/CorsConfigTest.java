package com.mobilia.contracts.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.mock;

class CorsConfigTest {

    private CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
    }

    @Test
    void shouldCreateCorsWebFilter() {
        CorsWebFilter corsWebFilter = corsConfig.corsWebFilter();
        assertNotNull(corsWebFilter);
    }

    @Test
    void shouldAllowConfiguredFrontendOrigin() {
        CorsWebFilter corsWebFilter = corsConfig.corsWebFilter();

        MockServerHttpRequest request = MockServerHttpRequest
                .options("http://localhost:8080/api/v1/contracts")
                .header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "GET").build();

        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        WebFilterChain filterChain = mock(WebFilterChain.class);

        StepVerifier.create(corsWebFilter.filter(exchange, filterChain)).verifyComplete();

        assertEquals("http://localhost:5173", exchange.getResponse().getHeaders().getAccessControlAllowOrigin());

        List<HttpMethod> allowedMethods = exchange.getResponse().getHeaders().getAccessControlAllowMethods();
        assertTrue(allowedMethods.contains(HttpMethod.GET));

        verifyNoInteractions(filterChain);
    }

    @Test
    void shouldAllowRequestedHeaders() {
        CorsWebFilter corsWebFilter = corsConfig.corsWebFilter();
        MockServerHttpRequest request = MockServerHttpRequest
                        .options("http://localhost:8080/api/v1/contracts/search")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "Content-Type")
                        .build();

        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        WebFilterChain filterChain = mock(WebFilterChain.class);
        StepVerifier.create(corsWebFilter.filter(exchange, filterChain)).verifyComplete();
        assertEquals("http://localhost:5173", exchange.getResponse().getHeaders().getAccessControlAllowOrigin());
        List<String> allowedHeaders = exchange.getResponse().getHeaders().getAccessControlAllowHeaders();
        assertTrue(allowedHeaders.stream().anyMatch(header -> header.equalsIgnoreCase("Content-Type")));
        verifyNoInteractions(filterChain);
    }
}