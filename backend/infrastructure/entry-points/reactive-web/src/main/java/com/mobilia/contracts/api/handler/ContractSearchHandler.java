package com.mobilia.contracts.api.handler;

import com.mobilia.contracts.api.dto.ContractResponse;
import com.mobilia.contracts.api.dto.ErrorResponse;
import com.mobilia.contracts.model.exception.ContractNotFoundException;
import com.mobilia.contracts.usecase.contracts.GetContractsUseCase;
import com.mobilia.contracts.usecase.contracts.SearchContractsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ContractSearchHandler {

    private final GetContractsUseCase getContractsUseCase;
    private final SearchContractsUseCase searchContractsUseCase;

    public ContractSearchHandler(
            GetContractsUseCase getContractsUseCase,
            SearchContractsUseCase searchContractsUseCase
    ) {
        this.getContractsUseCase = getContractsUseCase;
        this.searchContractsUseCase = searchContractsUseCase;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {

        return getContractsUseCase.execute()
                .map(ContractResponse::from)
                .collectList()
                .flatMap(contracts ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(contracts)
                );
    }

    public Mono<ServerResponse> search(ServerRequest request) {

        String searchTerm = request.queryParam("q")
                .orElse("");

        if (searchTerm.isBlank()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new ErrorResponse(
                            "INVALID_SEARCH_TERM",
                            "Query parameter 'q' is required"
                    ));
        }

        return searchContractsUseCase.execute(searchTerm)
                .map(ContractResponse::from)
                .collectList()
                .flatMap(contracts ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(contracts)
                )
                .onErrorResume(
                        ContractNotFoundException.class,
                        exception ->
                                ServerResponse.status(HttpStatus.NOT_FOUND)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(
                                                "CONTRACTS_NOT_FOUND",
                                                exception.getMessage()
                                        ))
                );
    }
}