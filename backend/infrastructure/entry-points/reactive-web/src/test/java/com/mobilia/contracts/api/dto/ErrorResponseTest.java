package com.mobilia.contracts.api.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseWithExpectedValues() {
        ErrorResponse response = new ErrorResponse(
                "CONTRACTS_NOT_FOUND",
                "No contracts found for search term: Pepito"
        );

        assertEquals(
                "CONTRACTS_NOT_FOUND",
                response.code()
        );

        assertEquals(
                "No contracts found for search term: Pepito",
                response.message()
        );
    }
}