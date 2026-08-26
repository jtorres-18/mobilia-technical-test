package com.mobilia.contracts.usecase.contract;

import com.mobilia.contracts.model.contract.Contract;
import com.mobilia.contracts.model.gateways.ContractGateway;
import reactor.core.publisher.Flux;

public class GetContractsUseCase {

    private final ContractGateway contractGateway;

    public GetContractsUseCase(ContractGateway contractGateway) {
        this.contractGateway = contractGateway;
    }

    public Flux<Contract> execute() {
        return contractGateway.findAll();
    }
}