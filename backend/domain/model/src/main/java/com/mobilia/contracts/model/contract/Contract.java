package com.mobilia.contracts.model.contract;


import lombok.Builder;
import com.mobilia.contracts.model.party.ContractParty;
import com.mobilia.contracts.model.party.PartyRole;
import com.mobilia.contracts.model.person.Person;
import com.mobilia.contracts.model.property.Property;

import java.util.List;
import java.util.Optional;

@Builder(toBuilder = true)
public record Contract(
        Long id,
        String code,
        ContractStatus status,
        Property property,
        List<ContractParty> parties
) {

    public Contract {
        parties = parties == null
                ? List.of()
                : List.copyOf(parties);
    }

    public Optional<Person> tenant() {
        return parties.stream()
                .filter(party -> party.role() == PartyRole.TENANT)
                .map(ContractParty::person)
                .findFirst();
    }

    public List<Person> owners() {
        return personsByRole(PartyRole.OWNER);
    }

    public List<Person> guarantors() {
        return personsByRole(PartyRole.GUARANTOR);
    }

    private List<Person> personsByRole(PartyRole role) {
        return parties.stream()
                .filter(party -> party.role() == role)
                .map(ContractParty::person)
                .toList();
    }
}
