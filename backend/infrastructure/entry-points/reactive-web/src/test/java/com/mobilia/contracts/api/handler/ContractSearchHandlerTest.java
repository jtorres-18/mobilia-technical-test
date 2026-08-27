package com.mobilia.contracts.api.handler;

import com.mobilia.contracts.model.contract.Contract;
import com.mobilia.contracts.model.contract.ContractStatus;
import com.mobilia.contracts.model.exception.ContractNotFoundException;
import com.mobilia.contracts.model.party.ContractParty;
import com.mobilia.contracts.model.party.PartyRole;
import com.mobilia.contracts.model.person.Person;
import com.mobilia.contracts.model.property.Property;
import com.mobilia.contracts.model.property.PropertyType;
import com.mobilia.contracts.usecase.contracts.GetContractsUseCase;
import com.mobilia.contracts.usecase.contracts.SearchContractsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ContractSearchHandlerTest {

    private GetContractsUseCase getContractsUseCase;
    private SearchContractsUseCase searchContractsUseCase;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        getContractsUseCase = mock(GetContractsUseCase.class);
        searchContractsUseCase = mock(SearchContractsUseCase.class);

        ContractSearchHandler handler =
                new ContractSearchHandler(
                        getContractsUseCase,
                        searchContractsUseCase
                );

        RouterFunction<ServerResponse> routes =
                RouterFunctions.route()
                        .GET(
                                "/api/v1/contracts",
                                handler::findAll
                        )
                        .GET(
                                "/api/v1/contracts/search",
                                handler::search
                        )
                        .build();

        webTestClient = WebTestClient
                .bindToRouterFunction(routes)
                .build();
    }

    @Test
    void shouldReturnAllContracts() {
        Contract activeContract = createContract(
                1L,
                "MBL-A100",
                ContractStatus.ACTIVE,
                "Calle 10 # 35-20, Medellin",
                PropertyType.APARTMENT
        );

        Contract inactiveContract = createContract(
                2L,
                "MBL-A090",
                ContractStatus.INACTIVE,
                "Calle 10 # 35-20, Medellin",
                PropertyType.APARTMENT
        );

        when(getContractsUseCase.execute())
                .thenReturn(
                        Flux.just(
                                activeContract,
                                inactiveContract
                        )
                );

        webTestClient.get()
                .uri("/api/v1/contracts")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()")
                .isEqualTo(2)
                .jsonPath("$[0].contractCode")
                .isEqualTo("MBL-A100")
                .jsonPath("$[0].status")
                .isEqualTo("ACTIVE")
                .jsonPath("$[0].propertyAddress")
                .isEqualTo("Calle 10 # 35-20, Medellin")
                .jsonPath("$[0].propertyType")
                .isEqualTo("APARTMENT")
                .jsonPath("$[0].tenant")
                .isEqualTo("Ana Torres")
                .jsonPath("$[0].owners[0]")
                .isEqualTo("Carlos Restrepo")
                .jsonPath("$[0].guarantors[0]")
                .isEqualTo("Sofia Herrera")
                .jsonPath("$[1].contractCode")
                .isEqualTo("MBL-A090")
                .jsonPath("$[1].status")
                .isEqualTo("INACTIVE");

        verify(getContractsUseCase).execute();
    }

    @Test
    void shouldReturnEmptyListWhenNoContractsExist() {
        when(getContractsUseCase.execute())
                .thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/v1/contracts")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("[]");

        verify(getContractsUseCase).execute();
    }

    @Test
    void shouldReturnContractsWhenSearchFindsResults() {
        Contract contract = createContract(
                1L,
                "MBL-A100",
                ContractStatus.ACTIVE,
                "Calle 10 # 35-20, Medellin",
                PropertyType.APARTMENT
        );

        when(searchContractsUseCase.execute("Ana"))
                .thenReturn(Flux.just(contract));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/contracts/search")
                                .queryParam("q", "Ana")
                                .build()
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()")
                .isEqualTo(1)
                .jsonPath("$[0].contractCode")
                .isEqualTo("MBL-A100")
                .jsonPath("$[0].status")
                .isEqualTo("ACTIVE")
                .jsonPath("$[0].tenant")
                .isEqualTo("Ana Torres")
                .jsonPath("$[0].owners[0]")
                .isEqualTo("Carlos Restrepo")
                .jsonPath("$[0].guarantors[0]")
                .isEqualTo("Sofia Herrera");

        verify(searchContractsUseCase)
                .execute("Ana");
    }

    @Test
    void shouldReturnBadRequestWhenSearchTermIsMissing() {
        webTestClient.get()
                .uri("/api/v1/contracts/search")
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.code")
                .isEqualTo("INVALID_SEARCH_TERM")
                .jsonPath("$.message")
                .isEqualTo(
                        "Query parameter 'q' is required"
                );

        verifyNoInteractions(searchContractsUseCase);
    }

    @Test
    void shouldReturnBadRequestWhenSearchTermIsBlank() {
        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/contracts/search")
                                .queryParam("q", "   ")
                                .build()
                )
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.code")
                .isEqualTo("INVALID_SEARCH_TERM")
                .jsonPath("$.message")
                .isEqualTo(
                        "Query parameter 'q' is required"
                );

        verifyNoInteractions(searchContractsUseCase);
    }

    @Test
    void shouldReturnNotFoundWhenSearchDoesNotFindContracts() {
        when(searchContractsUseCase.execute("Pepito"))
                .thenReturn(
                        Flux.error(
                                new ContractNotFoundException(
                                        "Pepito"
                                )
                        )
                );

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/contracts/search")
                                .queryParam("q", "Pepito")
                                .build()
                )
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.code")
                .isEqualTo("CONTRACTS_NOT_FOUND")
                .jsonPath("$.message")
                .isEqualTo(
                        "No contracts found for search term: Pepito"
                );

        verify(searchContractsUseCase)
                .execute("Pepito");
    }

    private Contract createContract(
            Long id,
            String code,
            ContractStatus status,
            String address,
            PropertyType propertyType
    ) {
        Person tenant = Person.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Torres")
                .identityDocument("1035001003")
                .email("ana.torres@example.com")
                .build();

        Person owner = Person.builder()
                .id(2L)
                .firstName("Carlos")
                .lastName("Restrepo")
                .identityDocument("1035001002")
                .email("carlos.restrepo@example.com")
                .build();

        Person guarantor = Person.builder()
                .id(3L)
                .firstName("Sofia")
                .lastName("Herrera")
                .identityDocument("1035001005")
                .email("sofia.herrera@example.com")
                .build();

        Property property = Property.builder()
                .id(id)
                .address(address)
                .type(propertyType)
                .build();

        return Contract.builder()
                .id(id)
                .code(code)
                .status(status)
                .property(property)
                .parties(List.of(
                        ContractParty.builder()
                                .id(1L)
                                .person(tenant)
                                .role(PartyRole.TENANT)
                                .build(),
                        ContractParty.builder()
                                .id(2L)
                                .person(owner)
                                .role(PartyRole.OWNER)
                                .build(),
                        ContractParty.builder()
                                .id(3L)
                                .person(guarantor)
                                .role(PartyRole.GUARANTOR)
                                .build()
                ))
                .build();
    }
}