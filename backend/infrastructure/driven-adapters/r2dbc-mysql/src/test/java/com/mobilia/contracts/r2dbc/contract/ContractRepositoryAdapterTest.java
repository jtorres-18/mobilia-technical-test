package com.mobilia.contracts.r2dbc.contract;

import com.mobilia.contracts.model.contract.Contract;
import com.mobilia.contracts.model.contract.ContractStatus;
import com.mobilia.contracts.model.party.PartyRole;
import com.mobilia.contracts.model.property.PropertyType;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.argThat;

class ContractRepositoryAdapterTest {

    private DatabaseClient databaseClient;
    private DatabaseClient.GenericExecuteSpec executeSpec;
    private ContractRepositoryAdapter repositoryAdapter;

    @BeforeEach
    void setUp() {
        databaseClient = mock(DatabaseClient.class);
        executeSpec = mock(DatabaseClient.GenericExecuteSpec.class);
        repositoryAdapter = new ContractRepositoryAdapter(databaseClient);
        when(databaseClient.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyString(), any())).thenReturn(executeSpec);
    }

    @Test
    void shouldSearchContractsAndMapCompleteDomainObjects() {
        List<ContractRow> databaseRows = List.of(
                createRow(
                        1L,
                        "MBL-A100",
                        "ACTIVE",
                        1L,
                        "Calle 10 # 35-20, Medellin",
                        "APARTMENT",
                        1L,
                        "TENANT",
                        1L,
                        "Ana",
                        "Torres",
                        "1035001003",
                        "ana.torres@example.com"
                ),
                createRow(
                        1L,
                        "MBL-A100",
                        "ACTIVE",
                        1L,
                        "Calle 10 # 35-20, Medellin",
                        "APARTMENT",
                        2L,
                        "OWNER",
                        2L,
                        "Carlos",
                        "Restrepo",
                        "1035001002",
                        "carlos.restrepo@example.com"
                ),
                createRow(
                        1L,
                        "MBL-A100",
                        "ACTIVE",
                        1L,
                        "Calle 10 # 35-20, Medellin",
                        "APARTMENT",
                        3L,
                        "GUARANTOR",
                        3L,
                        "Sofia",
                        "Herrera",
                        "1035001005",
                        "sofia.herrera@example.com"
                ),
                createRow(
                        2L,
                        "MBL-A090",
                        "INACTIVE",
                        2L,
                        "Carrera 70 # 45-18, Medellin",
                        "HOUSE",
                        4L,
                        "TENANT",
                        4L,
                        "Mateo",
                        "Ruiz",
                        "1035001004",
                        "mateo.ruiz@example.com"
                ),
                createRow(
                        2L,
                        "MBL-A090",
                        "INACTIVE",
                        2L,
                        "Carrera 70 # 45-18, Medellin",
                        "HOUSE",
                        5L,
                        "OWNER",
                        5L,
                        "Laura",
                        "Gomez",
                        "1035001001",
                        "laura.gomez@example.com"
                )
        );

        configureRows(databaseRows);

        StepVerifier.create(repositoryAdapter.search("   ANA   "))
                .assertNext(this::assertActiveApartmentContract)
                .assertNext(this::assertInactiveHouseContract)
                .verifyComplete();

        verify(executeSpec).bind("searchTerm", "ana");
        verify(executeSpec).bind("addressPattern", "ana%");
    }

    private void assertActiveApartmentContract(Contract contract) {

        assertEquals(1L, contract.id());
        assertEquals("MBL-A100", contract.code());
        assertEquals(ContractStatus.ACTIVE, contract.status());
        assertEquals(1L, contract.property().id());
        assertEquals("Calle 10 # 35-20, Medellin", contract.property().address());
        assertEquals(PropertyType.APARTMENT, contract.property().type());
        assertEquals(3, contract.parties().size());
        assertTrue(contract.tenant().isPresent());
        assertEquals("Ana Torres", contract.tenant().orElseThrow().fullName());
        assertEquals(1, contract.owners().size());
        assertEquals("Carlos Restrepo", contract.owners().getFirst().fullName());
        assertEquals(1, contract.guarantors().size());
        assertEquals("Sofia Herrera", contract.guarantors().getFirst().fullName());
        assertEquals(PartyRole.TENANT, contract.parties().get(0).role());
        assertEquals(PartyRole.OWNER, contract.parties().get(1).role());
        assertEquals(PartyRole.GUARANTOR, contract.parties().get(2).role());
    }

    private void assertInactiveHouseContract(Contract contract) {

        assertEquals(2L, contract.id());
        assertEquals("MBL-A090", contract.code());
        assertEquals(ContractStatus.INACTIVE, contract.status());
        assertEquals("Carrera 70 # 45-18, Medellin", contract.property().address());
        assertEquals(PropertyType.HOUSE, contract.property().type());
        assertEquals(2, contract.parties().size());
        assertTrue(contract.tenant().isPresent());
        assertEquals("Mateo Ruiz", contract.tenant().orElseThrow().fullName());
        assertEquals("Laura Gomez", contract.owners().getFirst().fullName());
        assertTrue(contract.guarantors().isEmpty());
    }

    @Test
    void shouldReturnEmptyFluxWhenSearchDoesNotReturnRows() {
        configureRows(List.of());
        StepVerifier.create(repositoryAdapter.search("Pepito")).verifyComplete();
        verify(executeSpec).bind("searchTerm", "pepito");
        verify(executeSpec).bind("addressPattern", "pepito%");
    }

    @Test
    void shouldFindAllContractsAndMapRows() {
        List<ContractRow> databaseRows = List.of(
                createRow(
                        3L,
                        "MBL-L300",
                        "INACTIVE",
                        3L,
                        "Calle 33 # 80-15, Medellin",
                        "COMMERCIAL_SPACE",
                        6L,
                        "TENANT",
                        6L,
                        "Miguel",
                        "Rojas",
                        "1035001008",
                        "miguel.rojas@example.com"
                ),
                createRow(
                        3L,
                        "MBL-L300",
                        "INACTIVE",
                        3L,
                        "Calle 33 # 80-15, Medellin",
                        "COMMERCIAL_SPACE",
                        7L,
                        "OWNER",
                        7L,
                        "Juliana",
                        "Velez",
                        "1035001009",
                        "juliana.velez@example.com"
                )
        );

        configureRows(databaseRows);

        StepVerifier.create(repositoryAdapter.findAll())
                .assertNext(contract -> {
                    assertEquals(3L, contract.id());
                    assertEquals("MBL-L300", contract.code());
                    assertEquals(ContractStatus.INACTIVE, contract.status());
                    assertEquals(PropertyType.COMMERCIAL_SPACE, contract.property().type());
                    assertEquals("Calle 33 # 80-15, Medellin", contract.property().address());
                    assertEquals(2, contract.parties().size());
                    assertEquals("Miguel Rojas", contract.tenant().orElseThrow().fullName());
                    assertEquals("Juliana Velez", contract.owners().getFirst().fullName());
                    assertTrue(contract.guarantors().isEmpty());
                })

                .verifyComplete();

        verify(databaseClient).sql(argThat((String query) ->
                query.contains("FROM contracts c") && !query.contains("WHERE")));
    }

    @Test
    void shouldReturnEmptyFluxWhenNoContractsExist() {
        configureRows(List.of());

        StepVerifier.create(repositoryAdapter.findAll()).verifyComplete();
    }

    @SuppressWarnings("unchecked")
    private void configureRows(
            List<ContractRow> databaseRows
    ) {
        doAnswer(invocation -> {

                    BiFunction<Row, RowMetadata, ContractRow> mapper = invocation.getArgument(0);
                    RowMetadata metadata = mock(RowMetadata.class);
                    List<ContractRow> mappedRows = databaseRows.stream()
                                    .map(contractRow -> mapper.apply(createMockRow(contractRow), metadata))
                                    .toList();

                    RowsFetchSpec<ContractRow> rowsFetchSpec = mock(RowsFetchSpec.class);
                    when(rowsFetchSpec.all()).thenReturn(Flux.fromIterable(mappedRows));
                    return rowsFetchSpec;

                })
                .when(executeSpec).map(Mockito.<BiFunction<Row, RowMetadata, ContractRow>>any());
    }

    private Row createMockRow(ContractRow contractRow) {

        Row row = mock(Row.class);
        when(row.get("contract_id", Long.class)).thenReturn(contractRow.contractId());
        when(row.get("contract_code", String.class)).thenReturn(contractRow.contractCode());
        when(row.get("contract_status", String.class)).thenReturn(contractRow.contractStatus());
        when(row.get("property_id", Long.class)).thenReturn(contractRow.propertyId());
        when(row.get("property_address", String.class)).thenReturn(contractRow.propertyAddress());
        when(row.get("property_type", String.class)).thenReturn(contractRow.propertyType());
        when(row.get("party_id", Long.class)).thenReturn(contractRow.partyId());
        when(row.get("party_role", String.class)).thenReturn(contractRow.partyRole());
        when(row.get("person_id", Long.class)).thenReturn(contractRow.personId());
        when(row.get("first_name", String.class)).thenReturn(contractRow.firstName());
        when(row.get("last_name", String.class)).thenReturn(contractRow.lastName());
        when(row.get("identity_document", String.class)).thenReturn(contractRow.identityDocument());
        when(row.get("email", String.class)).thenReturn(contractRow.email());
        return row;
    }

    private ContractRow createRow(
            Long contractId,
            String contractCode,
            String contractStatus,
            Long propertyId,
            String propertyAddress,
            String propertyType,
            Long partyId,
            String partyRole,
            Long personId,
            String firstName,
            String lastName,
            String identityDocument,
            String email
    ) {
        return new ContractRow(
                contractId,
                contractCode,
                contractStatus,
                propertyId,
                propertyAddress,
                propertyType,
                partyId,
                partyRole,
                personId,
                firstName,
                lastName,
                identityDocument,
                email
        );
    }
}