package com.mobilia.contracts.model.gateways;

import com.mobilia.contracts.model.contract.Contract;
import reactor.core.publisher.Flux;

public interface ContractGateway {

    Flux<Contract> search(String searchTerm);
}
