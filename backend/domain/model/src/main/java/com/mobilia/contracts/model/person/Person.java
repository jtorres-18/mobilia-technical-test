package com.mobilia.contracts.model.person;

import lombok.Builder;

@Builder(toBuilder = true)
public record Person(
        Long id,
        String firstName,
        String lastName,
        String identityDocument,
        String email
) {

    public String fullName() {
        return firstName + " " + lastName;
    }
}