package com.mobilia.contracts.api.dto;

import com.mobilia.contracts.model.contract.Contract;
import com.mobilia.contracts.model.person.Person;

import java.util.List;

public record ContractResponse(
        String contractCode,
        String status,
        String propertyAddress,
        String propertyType,
        String tenant,
        List<String> owners,
        List<String> guarantors
) {

    public static ContractResponse from(Contract contract) {
        return new ContractResponse(
                contract.code(),
                contract.status().name(),
                contract.property().address(),
                contract.property().type().name(),
                contract.tenant()
                        .map(Person::fullName)
                        .orElse(""),
                contract.owners().stream()
                        .map(Person::fullName)
                        .toList(),
                contract.guarantors().stream()
                        .map(Person::fullName)
                        .toList()
        );
    }
}