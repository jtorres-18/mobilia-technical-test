package com.mobilia.contracts.model.party;

import com.mobilia.contracts.model.person.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class ContractPartyTest {

    @Test
    void shouldCreateContractPartyWithExpectedValues() {
        Person person = createPerson();

        ContractParty contractParty = new ContractParty(
                1L,
                person,
                PartyRole.TENANT
        );

        assertEquals(1L, contractParty.id());
        assertEquals(person, contractParty.person());
        assertEquals(PartyRole.TENANT, contractParty.role());
    }

    @Test
    void shouldCreateContractPartyUsingBuilder() {
        Person person = createPerson();

        ContractParty contractParty = ContractParty.builder()
                .id(1L)
                .person(person)
                .role(PartyRole.OWNER)
                .build();

        assertEquals(1L, contractParty.id());
        assertEquals(person, contractParty.person());
        assertEquals(PartyRole.OWNER, contractParty.role());
    }

    @Test
    void shouldCreateModifiedCopyUsingToBuilder() {
        Person person = createPerson();

        ContractParty original = ContractParty.builder()
                .id(1L)
                .person(person)
                .role(PartyRole.OWNER)
                .build();

        ContractParty modified = original.toBuilder()
                .role(PartyRole.GUARANTOR)
                .build();

        assertNotSame(original, modified);

        assertEquals(original.id(), modified.id());
        assertEquals(original.person(), modified.person());

        assertEquals(PartyRole.OWNER, original.role());
        assertEquals(PartyRole.GUARANTOR, modified.role());
    }

    private Person createPerson() {
        return new Person(
                1L,
                "Laura",
                "Gomez",
                "1035001001",
                "laura.gomez@example.com"
        );
    }
}