package com.mobilia.contracts.r2dbc.contract;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContractRowTest {

    @Test
    void shouldCreateContractRowWithExpectedValues() {
        ContractRow row = new ContractRow(
                1L,
                "MBL-A100",
                "ACTIVE",
                10L,
                "Calle 10 # 35-20, Medellin",
                "APARTMENT",
                100L,
                "TENANT",
                1000L,
                "Ana",
                "Torres",
                "1035001003",
                "ana.torres@example.com"
        );

        assertEquals(1L, row.contractId());
        assertEquals("MBL-A100", row.contractCode());
        assertEquals("ACTIVE", row.contractStatus());

        assertEquals(10L, row.propertyId());
        assertEquals(
                "Calle 10 # 35-20, Medellin",
                row.propertyAddress()
        );
        assertEquals("APARTMENT", row.propertyType());

        assertEquals(100L, row.partyId());
        assertEquals("TENANT", row.partyRole());

        assertEquals(1000L, row.personId());
        assertEquals("Ana", row.firstName());
        assertEquals("Torres", row.lastName());
        assertEquals("1035001003", row.identityDocument());
        assertEquals(
                "ana.torres@example.com",
                row.email()
        );
    }
}