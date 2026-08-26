package com.mobilia.contracts.r2dbc.contract;

import com.mobilia.contracts.model.contract.Contract;
import com.mobilia.contracts.model.contract.ContractStatus;
import com.mobilia.contracts.model.gateways.ContractGateway;
import com.mobilia.contracts.model.party.ContractParty;
import com.mobilia.contracts.model.party.PartyRole;
import com.mobilia.contracts.model.person.Person;
import com.mobilia.contracts.model.property.Property;
import com.mobilia.contracts.model.property.PropertyType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ContractRepositoryAdapter implements ContractGateway {

    private static final String SEARCH_QUERY = """
            SELECT
                c.id AS contract_id,
                c.code AS contract_code,
                c.status AS contract_status,

                p.id AS property_id,
                p.address AS property_address,
                p.type AS property_type,

                cp.id AS party_id,
                cp.role AS party_role,

                pe.id AS person_id,
                pe.first_name,
                pe.last_name,
                pe.identity_document,
                pe.email

            FROM contracts c

            INNER JOIN properties p
                ON p.id = c.property_id

            INNER JOIN contract_parties cp
                ON cp.contract_id = c.id

            INNER JOIN persons pe
                ON pe.id = cp.person_id

            WHERE
                LOWER(c.code) LIKE :pattern

                OR LOWER(p.address) LIKE :pattern

                OR EXISTS (
                    SELECT 1
                    FROM contract_parties cp_search

                    INNER JOIN persons pe_search
                        ON pe_search.id = cp_search.person_id

                    WHERE cp_search.contract_id = c.id
                      AND (
                          LOWER(pe_search.first_name) LIKE :pattern
                          OR LOWER(pe_search.last_name) LIKE :pattern
                          OR LOWER(
                              CONCAT_WS(
                                  ' ',
                                  pe_search.first_name,
                                  pe_search.last_name
                              )
                          ) LIKE :pattern
                          OR LOWER(pe_search.identity_document) LIKE :pattern
                          OR LOWER(pe_search.email) LIKE :pattern
                      )
                )

            ORDER BY
                CASE
                    WHEN c.status = 'ACTIVE' THEN 0
                    ELSE 1
                END,
                c.id DESC,
                cp.id
            """;

    private final DatabaseClient databaseClient;

    public ContractRepositoryAdapter(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<Contract> search(String searchTerm) {

        String pattern = "%" + searchTerm
                .toLowerCase(Locale.ROOT) + "%";

        return databaseClient
                .sql(SEARCH_QUERY)
                .bind("pattern", pattern)
                .map((row, metadata) -> new ContractRow(
                        row.get("contract_id", Long.class),
                        row.get("contract_code", String.class),
                        row.get("contract_status", String.class),

                        row.get("property_id", Long.class),
                        row.get("property_address", String.class),
                        row.get("property_type", String.class),

                        row.get("party_id", Long.class),
                        row.get("party_role", String.class),

                        row.get("person_id", Long.class),
                        row.get("first_name", String.class),
                        row.get("last_name", String.class),
                        row.get("identity_document", String.class),
                        row.get("email", String.class)
                ))
                .all()
                .collectList()
                .flatMapMany(this::mapContracts);
    }

    private Flux<Contract> mapContracts(List<ContractRow> rows) {

        Map<Long, List<ContractRow>> groupedContracts = rows.stream()
                .collect(Collectors.groupingBy(
                        ContractRow::contractId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return Flux.fromIterable(groupedContracts.values())
                .map(this::mapContract);
    }

    private Contract mapContract(List<ContractRow> rows) {

        ContractRow firstRow = rows.getFirst();

        Property property = Property.builder()
                .id(firstRow.propertyId())
                .address(firstRow.propertyAddress())
                .type(PropertyType.valueOf(firstRow.propertyType()))
                .build();

        List<ContractParty> parties = rows.stream()
                .map(this::mapContractParty)
                .toList();

        return Contract.builder()
                .id(firstRow.contractId())
                .code(firstRow.contractCode())
                .status(ContractStatus.valueOf(firstRow.contractStatus()))
                .property(property)
                .parties(parties)
                .build();
    }

    private ContractParty mapContractParty(ContractRow row) {

        Person person = Person.builder()
                .id(row.personId())
                .firstName(row.firstName())
                .lastName(row.lastName())
                .identityDocument(row.identityDocument())
                .email(row.email())
                .build();

        return ContractParty.builder()
                .id(row.partyId())
                .person(person)
                .role(PartyRole.valueOf(row.partyRole()))
                .build();
    }
}