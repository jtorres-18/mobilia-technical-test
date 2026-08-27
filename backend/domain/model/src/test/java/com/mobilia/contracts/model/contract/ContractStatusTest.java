package com.mobilia.contracts.model.contract;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ContractStatusTest {

    @Test
    void shouldContainExpectedStatuses() {
        ContractStatus[] expectedStatuses = {
                ContractStatus.ACTIVE,
                ContractStatus.INACTIVE
        };

        assertArrayEquals(
                expectedStatuses,
                ContractStatus.values()
        );
    }

    @Test
    void shouldReturnActiveStatusByName() {
        ContractStatus status =
                ContractStatus.valueOf("ACTIVE");

        assertEquals(
                ContractStatus.ACTIVE,
                status
        );
    }

    @Test
    void shouldReturnInactiveStatusByName() {
        ContractStatus status =
                ContractStatus.valueOf("INACTIVE");

        assertEquals(
                ContractStatus.INACTIVE,
                status
        );
    }
}