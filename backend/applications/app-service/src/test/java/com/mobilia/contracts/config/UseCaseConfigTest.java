package com.mobilia.contracts.config;

import com.mobilia.contracts.model.gateways.ContractGateway;
import com.mobilia.contracts.usecase.contracts.GetContractsUseCase;
import com.mobilia.contracts.usecase.contracts.SearchContractsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.mock;

class UseCaseConfigTest {

    private UseCaseConfig useCaseConfig;
    private ContractGateway contractGateway;

    @BeforeEach
    void setUp() {
        useCaseConfig = new UseCaseConfig();
        contractGateway = mock(ContractGateway.class);
    }

    @Test
    void shouldCreateSearchContractsUseCase() {
        SearchContractsUseCase useCase = useCaseConfig.searchContractsUseCase(contractGateway);
        assertNotNull(useCase);
    }

    @Test
    void shouldCreateGetContractsUseCase() {
        GetContractsUseCase useCase = useCaseConfig.getContractsUseCase(contractGateway);
        assertNotNull(useCase);
    }
}