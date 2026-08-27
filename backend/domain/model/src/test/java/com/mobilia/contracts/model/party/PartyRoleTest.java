package com.mobilia.contracts.model.party;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PartyRoleTest {

    @Test
    void shouldContainExpectedRoles() {
        PartyRole[] expectedRoles = {
                PartyRole.TENANT,
                PartyRole.OWNER,
                PartyRole.GUARANTOR
        };

        assertArrayEquals(expectedRoles, PartyRole.values());
    }

    @Test
    void shouldReturnTenantRoleByName() {
        PartyRole role = PartyRole.valueOf("TENANT");

        assertEquals(
                PartyRole.TENANT,
                role
        );
    }

    @Test
    void shouldReturnOwnerRoleByName() {
        PartyRole role = PartyRole.valueOf("OWNER");

        assertEquals(
                PartyRole.OWNER,
                role
        );
    }

    @Test
    void shouldReturnGuarantorRoleByName() {
        PartyRole role = PartyRole.valueOf("GUARANTOR");

        assertEquals(
                PartyRole.GUARANTOR,
                role
        );
    }
}