package com.mobilia.contracts.usecase.contracts;

import com.mobilia.contracts.model.contract.Contract;
import com.mobilia.contracts.model.gateways.ContractGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GetContractsUseCaseTest {

    private ContractGateway contractGateway;
    private GetContractsUseCase getContractsUseCase;

    @BeforeEach
    void setUp() {
        contractGateway = mock(ContractGateway.class);
        getContractsUseCase = new GetContractsUseCase(contractGateway);
    }

    @Test
    void shouldReturnAllContracts() {
        Contract firstContract = mock(Contract.class);
        Contract secondContract = mock(Contract.class);

        when(contractGateway.findAll())
                .thenReturn(Flux.just(firstContract, secondContract));

        StepVerifier.create(getContractsUseCase.execute())
                .expectNext(firstContract)
                .expectNext(secondContract)
                .verifyComplete();

        verify(contractGateway).findAll();
    }

    @Test
    void shouldReturnEmptyFluxWhenNoContractsExist() {
        when(contractGateway.findAll()).thenReturn(Flux.empty());
        StepVerifier.create(getContractsUseCase.execute()).verifyComplete();
        verify(contractGateway).findAll();
    }

    @Test
    void shouldPropagateGatewayError() {
        RuntimeException expectedException = new RuntimeException("Database connection error");
        when(contractGateway.findAll()).thenReturn(Flux.error(expectedException));

        StepVerifier.create(getContractsUseCase.execute())
                .expectErrorMatches(error ->
                        error == expectedException && error.getMessage().equals("Database connection error")
                )
                .verify();

        verify(contractGateway).findAll();
    }
}