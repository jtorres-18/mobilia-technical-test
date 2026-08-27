package com.mobilia.contracts.model.contract;

import com.mobilia.contracts.model.party.ContractParty;
import com.mobilia.contracts.model.party.PartyRole;
import com.mobilia.contracts.model.person.Person;
import com.mobilia.contracts.model.property.Property;
import com.mobilia.contracts.model.property.PropertyType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContractTest {

    @Test
    void shouldUseEmptyListWhenPartiesIsNull() {
        Contract contract = Contract.builder()
                .id(1L)
                .code("MBL-A100")
                .status(ContractStatus.ACTIVE)
                .property(createProperty())
                .parties(null)
                .build();

        assertTrue(contract.parties().isEmpty());
    }

    @Test
    void shouldCreateImmutableCopyOfParties() {
        Person tenant = createPerson(1L, "Laura", "Gomez", "1035001001",
                "laura.gomez@example.com");

        ContractParty tenantParty = new ContractParty(1L, tenant, PartyRole.TENANT);
        List<ContractParty> mutableParties = new ArrayList<>();
        mutableParties.add(tenantParty);
        Contract contract = Contract.builder()
                .id(1L)
                .code("MBL-A100")
                .status(ContractStatus.ACTIVE)
                .property(createProperty())
                .parties(mutableParties)
                .build();

        mutableParties.clear();

        List<ContractParty> immutableParties = contract.parties();
        assertEquals(1, contract.parties().size());
        assertThrows(UnsupportedOperationException.class, () ->immutableParties.add(tenantParty));
    }

    @Test
    void shouldReturnTenantWhenContractHasTenant() {
        Person tenant = createPerson(1L, "Laura", "Gomez", "1035001001",
                "laura.gomez@example.com");

        Person owner = createPerson(2L, "Carlos", "Restrepo", "1035001002",
                "carlos.restrepo@example.com");

        Contract contract = createContract(List.of(new ContractParty(1L, tenant, PartyRole.TENANT),
                        new ContractParty(2L, owner, PartyRole.OWNER)));

        Optional<Person> result = contract.tenant();

        assertTrue(result.isPresent());
        assertEquals(tenant, result.get());
    }

    @Test
    void shouldReturnEmptyTenantWhenContractHasNoTenant() {
        Person owner = createPerson(2L, "Carlos", "Restrepo", "1035001002",
                "carlos.restrepo@example.com");

        Contract contract = createContract(List.of(new ContractParty(1L, owner, PartyRole.OWNER)));

        Optional<Person> result = contract.tenant();

        assertFalse(result.isPresent());
    }

    @Test
    void shouldReturnAllOwners() {
        Person tenant = createPerson(1L, "Laura", "Gomez", "1035001001",
                "laura.gomez@example.com");

        Person firstOwner = createPerson(2L, "Carlos", "Restrepo", "1035001002",
                "carlos.restrepo@example.com");

        Person secondOwner = createPerson(3L, "Ana", "Torres", "1035001003",
                "ana.torres@example.com");

        Person guarantor = createPerson(4L, "Sofia", "Herrera", "1035001005",
                "sofia.herrera@example.com");

        Contract contract = createContract(List.of(new ContractParty(1L, tenant, PartyRole.TENANT),
                        new ContractParty(2L, firstOwner, PartyRole.OWNER),
                        new ContractParty(3L, secondOwner, PartyRole.OWNER),
                        new ContractParty(4L, guarantor, PartyRole.GUARANTOR)));
        List<Person> owners = contract.owners();
        assertEquals(2, owners.size());
        assertEquals(List.of(firstOwner, secondOwner), owners);
    }

    @Test
    void shouldReturnEmptyOwnersWhenContractHasNoOwners() {
        Person tenant = createPerson(1L, "Laura", "Gomez", "1035001001",
                "laura.gomez@example.com");

        Contract contract = createContract(List.of(new ContractParty(1L, tenant, PartyRole.TENANT)));

        List<Person> owners = contract.owners();

        assertTrue(owners.isEmpty());
    }

    @Test
    void shouldReturnAllGuarantors() {
        Person tenant = createPerson(1L, "Laura", "Gomez", "1035001001",
                "laura.gomez@example.com");

        Person firstGuarantor = createPerson(2L, "Sofia", "Herrera", "1035001005",
                "sofia.herrera@example.com");

        Person secondGuarantor = createPerson(3L, "Daniel", "Castro", "1035001006",
                "daniel.castro@example.com");

        Contract contract = createContract(List.of(new ContractParty(1L, tenant, PartyRole.TENANT),
                        new ContractParty(2L, firstGuarantor, PartyRole.GUARANTOR),
                        new ContractParty(3L, secondGuarantor, PartyRole.GUARANTOR)));

        List<Person> guarantors = contract.guarantors();

        assertEquals(2, guarantors.size());
        assertEquals(List.of(firstGuarantor, secondGuarantor), guarantors);
    }

    @Test
    void shouldReturnEmptyGuarantorsWhenContractHasNoGuarantors() {
        Person tenant = createPerson(1L, "Laura", "Gomez", "1035001001",
                "laura.gomez@example.com");

        Person owner = createPerson(2L, "Carlos", "Restrepo", "1035001002",
                "carlos.restrepo@example.com");

        Contract contract = createContract(List.of(new ContractParty(1L, tenant, PartyRole.TENANT),
                        new ContractParty(2L, owner, PartyRole.OWNER)));

        List<Person> guarantors = contract.guarantors();
        assertTrue(guarantors.isEmpty());
    }

    private Contract createContract(List<ContractParty> parties) {
        return Contract.builder().id(1L).code("MBL-A100").status(ContractStatus.ACTIVE).property(createProperty())
                .parties(parties)
                .build();
    }

    private Property createProperty() {
        return new Property(1L, "Calle 10 # 35-20, Medellin", PropertyType.APARTMENT);
    }

    private Person createPerson(Long id, String firstName, String lastName, String identityDocument, String email) {
        return new Person(id, firstName, lastName, identityDocument, email);
    }
}