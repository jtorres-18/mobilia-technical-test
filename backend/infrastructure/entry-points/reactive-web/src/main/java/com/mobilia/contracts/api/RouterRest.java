package com.mobilia.contracts.api;

import com.mobilia.contracts.api.handler.ContractSearchHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    private final String basePath;

    public RouterRest(
            @Value("${api.contracts.base-path}") String basePath
    ) {
        this.basePath = basePath;
    }

    @Bean
    public RouterFunction<ServerResponse> contractRoutes(
            ContractSearchHandler handler
    ) {
        return route()
                .GET(basePath, handler::findAll)
                .GET(basePath + "/search", handler::search)
                .build();
    }
}