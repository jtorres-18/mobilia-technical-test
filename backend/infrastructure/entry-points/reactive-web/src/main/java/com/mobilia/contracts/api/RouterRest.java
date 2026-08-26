package com.mobilia.contracts.api;

import com.mobilia.contracts.api.handler.ContractSearchHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    private static final String BASE_PATH = "/api/v1/contracts";

    @Bean
    public RouterFunction<ServerResponse> contractRoutes(
            ContractSearchHandler handler
    ) {
        return route()
                .GET(BASE_PATH, handler::findAll)
                .GET(BASE_PATH + "/search", handler::search)
                .build();
    }
}