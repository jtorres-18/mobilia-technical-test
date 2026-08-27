package com.mobilia.contracts.api.dto;

import com.mobilia.contracts.model.contract.Contract;
import com.mobilia.contracts.model.contract.ContractStatus;
import com.mobilia.contracts.model.party.ContractParty;
import com.mobilia.contracts.model.party.PartyRole;
import com.mobilia.contracts.model.person.Person;
import com.mobilia.contracts.model.property.Property;
import com.mobilia.contracts.model.property.PropertyType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContractResponseTest {

    @Test
    void shouldMapContractToResponseWithAllParticipants() {
        Person tenant = createPerson(
                1L,
                "Ana",
                "Torres",
                "1035001003",
                "ana.torres@example.com"
        );

        Person firstOwner = createPerson(
                2L,
                "Carlos",
                "Restrepo",
                "1035001002",
                "carlos.restrepo@example.com"
        );

        Person secondOwner = createPerson(
                3L,
                "Laura",
                "Gomez",
                "1035001001",
                "laura.gomez@example.com"
        );

        Person guarantor = createPerson(
                4L,
                "Sofia",
                "Herrera",
                "1035001005",
                "sofia.herrera@example.com"
        );

        Property property = Property.builder()
                .id(1L)
                .address("Calle 10 # 35-20, Medellin")
                .type(PropertyType.APARTMENT)
                .build();

        Contract contract = Contract.builder()
                .id(1L)
                .code("MBL-A100")
                .status(ContractStatus.ACTIVE)
                .property(property)
                .parties(List.of(
                        new ContractParty(
                                1L,
                                tenant,
                                PartyRole.TENANT
                        ),
                        new ContractParty(
                                2L,
                                firstOwner,
                                PartyRole.OWNER
                        ),
                        new ContractParty(
                                3L,
                                secondOwner,
                                PartyRole.OWNER
                        ),
                        new ContractParty(
                                4L,
                                guarantor,
                                PartyRole.GUARANTOR
                        )
                ))
                .build();

        ContractResponse response =
                ContractResponse.from(contract);

        assertEquals(
                "MBL-A100",
                response.contractCode()
        );

        assertEquals(
                "ACTIVE",
                response.status()
        );

        assertEquals(
                "Calle 10 # 35-20, Medellin",
                response.propertyAddress()
        );

        assertEquals(
                "APARTMENT",
                response.propertyType()
        );

        assertEquals(
                "Ana Torres",
                response.tenant()
        );

        assertEquals(
                List.of(
                        "Carlos Restrepo",
                        "Laura Gomez"
                ),
                response.owners()
        );

        assertEquals(
                List.of("Sofia Herrera"),
                response.guarantors()
        );
    }

    @Test
    void shouldMapContractWithoutTenantOrGuarantors() {
        Person owner = createPerson(
                2L,
                "Carlos",
                "Restrepo",
                "1035001002",
                "carlos.restrepo@example.com"
        );

        Property property = Property.builder()
                .id(2L)
                .address("Carrera 70 # 45-18, Medellin")
                .type(PropertyType.HOUSE)
                .build();

        Contract contract = Contract.builder()
                .id(2L)
                .code("MBL-A090")
                .status(ContractStatus.INACTIVE)
                .property(property)
                .parties(List.of(
                        new ContractParty(
                                5L,
                                owner,
                                PartyRole.OWNER
                        )
                ))
                .build();

        ContractResponse response =
                ContractResponse.from(contract);

        assertEquals(
                "MBL-A090",
                response.contractCode()
        );

        assertEquals(
                "INACTIVE",
                response.status()
        );

        assertEquals(
                "Carrera 70 # 45-18, Medellin",
                response.propertyAddress()
        );

        assertEquals(
                "HOUSE",
                response.propertyType()
        );

        assertEquals(
                "",
                response.tenant()
        );

        assertEquals(
                List.of("Carlos Restrepo"),
                response.owners()
        );

        assertTrue(
                response.guarantors().isEmpty()
        );
    }

    @Test
    void shouldCreateContractResponseDirectly() {
        ContractResponse response =
                new ContractResponse(
                        "MBL-L300",
                        "ACTIVE",
                        "Calle 33 # 80-15, Medellin",
                        "COMMERCIAL_SPACE",
                        "Miguel Rojas",
                        List.of("Juliana Velez"),
                        List.of("Sofia Herrera")
                );

        assertEquals(
                "MBL-L300",
                response.contractCode()
        );

        assertEquals(
                "ACTIVE",
                response.status()
        );

        assertEquals(
                "Calle 33 # 80-15, Medellin",
                response.propertyAddress()
        );

        assertEquals(
                "COMMERCIAL_SPACE",
                response.propertyType()
        );

        assertEquals(
                "Miguel Rojas",
                response.tenant()
        );

        assertEquals(
                List.of("Juliana Velez"),
                response.owners()
        );

        assertEquals(
                List.of("Sofia Herrera"),
                response.guarantors()
        );
    }

    private Person createPerson(
            Long id,
            String firstName,
            String lastName,
            String identityDocument,
            String email
    ) {
        return Person.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .identityDocument(identityDocument)
                .email(email)
                .build();
    }
}