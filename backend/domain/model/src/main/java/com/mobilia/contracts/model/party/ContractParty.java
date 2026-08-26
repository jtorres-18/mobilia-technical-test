package com.mobilia.contracts.model.party;

import lombok.Builder;
import com.mobilia.contracts.model.person.Person;

@Builder(toBuilder = true)
public record ContractParty(
        Long id,
        Person person,
        PartyRole role
) {
}
