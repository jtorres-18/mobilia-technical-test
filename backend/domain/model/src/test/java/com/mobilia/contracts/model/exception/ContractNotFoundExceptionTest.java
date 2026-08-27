package com.mobilia.contracts.model.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContractNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithSearchTermInMessage() {
        String searchTerm = "Pepito";

        ContractNotFoundException exception =
                new ContractNotFoundException(searchTerm);

        assertEquals(
                "No contracts found for search term: Pepito",
                exception.getMessage()
        );
    }
}