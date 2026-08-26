package com.mobilia.contracts.api.dto;

public record ErrorResponse(
        String code,
        String message
) {
}