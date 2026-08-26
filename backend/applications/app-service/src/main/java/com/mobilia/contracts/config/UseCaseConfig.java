package com.mobilia.contracts.config;

import com.mobilia.contracts.model.gateways.ContractGateway;
import com.mobilia.contracts.usecase.contract.SearchContractsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public SearchContractsUseCase searchContractsUseCase(
            ContractGateway contractGateway
    ) {
        return new SearchContractsUseCase(contractGateway);
    }
}