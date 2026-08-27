package com.mobilia.contracts.usecase.contracts;

import com.mobilia.contracts.model.contract.Contract;
import com.mobilia.contracts.model.exception.ContractNotFoundException;
import com.mobilia.contracts.model.gateways.ContractGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class SearchContractsUseCaseTest {

    private ContractGateway contractGateway;
    private SearchContractsUseCase searchContractsUseCase;

    @BeforeEach
    void setUp() {
        contractGateway = mock(ContractGateway.class);
        searchContractsUseCase = new SearchContractsUseCase(contractGateway);
    }

    @Test
    void shouldReturnContractsWhenSearchFindsResults() {
        Contract contract = mock(Contract.class);

        when(contractGateway.search("Ana")).thenReturn(Flux.just(contract));

        StepVerifier.create(searchContractsUseCase.execute("Ana"))
                .expectNext(contract)
                .verifyComplete();
        verify(contractGateway).search("Ana");
    }

    @Test
    void shouldReturnMultipleContractsWhenSearchFindsMoreThanOneResult() {
        Contract firstContract = mock(Contract.class);
        Contract secondContract = mock(Contract.class);

        when(contractGateway.search("Ana")).thenReturn(Flux.just(firstContract, secondContract));

        StepVerifier.create(searchContractsUseCase.execute("Ana"))
                .expectNext(firstContract)
                .expectNext(secondContract)
                .verifyComplete();

        verify(contractGateway).search("Ana");
    }

    @Test
    void shouldTrimSearchTermBeforeCallingGateway() {
        Contract contract = mock(Contract.class);

        when(contractGateway.search("Ana")).thenReturn(Flux.just(contract));

        StepVerifier.create(searchContractsUseCase.execute("   Ana   "))
                .expectNext(contract)
                .verifyComplete();

        verify(contractGateway).search("Ana");
    }

    @Test
    void shouldThrowContractNotFoundExceptionWhenNoContractsAreFound() {
        when(contractGateway.search("Pepito")).thenReturn(Flux.empty());

        StepVerifier.create(searchContractsUseCase.execute("Pepito"))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(ContractNotFoundException.class, error);
                    assertEquals("No contracts found for search term: Pepito", error.getMessage());
                })
                .verify();

        verify(contractGateway).search("Pepito");
    }

    @Test
    void shouldUseTrimmedTermInNotFoundException() {
        when(contractGateway.search("Pepito")).thenReturn(Flux.empty());

        StepVerifier.create(searchContractsUseCase.execute("   Pepito   "))
                .expectErrorMatches(error ->
                        error instanceof ContractNotFoundException && error.getMessage()
                                .equals("No contracts found for search term: Pepito"))
                .verify();

        verify(contractGateway).search("Pepito");
    }

    @Test
    void shouldPropagateGatewayError() {
        RuntimeException expectedException = new RuntimeException("Database connection error");

        when(contractGateway.search("Ana")).thenReturn(Flux.error(expectedException));

        StepVerifier.create(searchContractsUseCase.execute("Ana"))
                .expectErrorMatches(error -> error == expectedException && error.getMessage()
                        .equals("Database connection error"))
                .verify();

        verify(contractGateway).search("Ana");
    }
}