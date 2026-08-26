package com.mobilia.contracts.model.property;

import lombok.Builder;

@Builder(toBuilder = true)
public record Property(
        Long id,
        String address,
        PropertyType type
) {
}
