package com.mobilia.contracts.usecase.contract;

import com.mobilia.contracts.model.contract.Contract;
import com.mobilia.contracts.model.exception.ContractNotFoundException;
import com.mobilia.contracts.model.gateways.ContractGateway;
import reactor.core.publisher.Flux;

public class SearchContractsUseCase {

    private final ContractGateway contractGateway;

    public SearchContractsUseCase(ContractGateway contractGateway) {
        this.contractGateway = contractGateway;
    }

    public Flux<Contract> execute(String searchTerm) {

        String normalizedTerm = searchTerm.trim();

        return contractGateway.search(normalizedTerm)
                .switchIfEmpty(
                        Flux.error(
                                new ContractNotFoundException(normalizedTerm)
                        )
                );
    }
}