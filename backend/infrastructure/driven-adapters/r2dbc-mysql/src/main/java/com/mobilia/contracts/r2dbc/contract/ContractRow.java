package com.mobilia.contracts.r2dbc.contract;

public record ContractRow(
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
}