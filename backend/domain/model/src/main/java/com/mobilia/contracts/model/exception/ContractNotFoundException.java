package com.mobilia.contracts.model.exception;

public class ContractNotFoundException extends RuntimeException {

    public ContractNotFoundException(String searchTerm) {
        super("No contracts found for search term: " + searchTerm);
    }
}
