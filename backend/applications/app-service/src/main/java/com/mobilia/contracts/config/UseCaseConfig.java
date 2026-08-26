package com.mobilia.contracts.config;

import com.mobilia.contracts.model.gateways.ContractGateway;
import com.mobilia.contracts.usecase.contracts.GetContractsUseCase;
import com.mobilia.contracts.usecase.contracts.SearchContractsUseCase;
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

    @Bean
    public GetContractsUseCase getContractsUseCase(
            ContractGateway contractGateway
    ) {
        return new GetContractsUseCase(contractGateway);
    }
}