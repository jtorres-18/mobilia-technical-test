package com.mobilia.contracts.model.property;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class PropertyTest {

    @Test
    void shouldCreatePropertyWithExpectedValues() {
        Property property = new Property(
                1L,
                "Calle 10 # 35-20, Medellin",
                PropertyType.APARTMENT
        );

        assertEquals(1L, property.id());
        assertEquals(
                "Calle 10 # 35-20, Medellin",
                property.address()
        );
        assertEquals(
                PropertyType.APARTMENT,
                property.type()
        );
    }

    @Test
    void shouldCreatePropertyUsingBuilder() {
        Property property = Property.builder()
                .id(1L)
                .address("Carrera 70 # 45-18, Medellin")
                .type(PropertyType.HOUSE)
                .build();

        assertEquals(1L, property.id());
        assertEquals(
                "Carrera 70 # 45-18, Medellin",
                property.address()
        );
        assertEquals(
                PropertyType.HOUSE,
                property.type()
        );
    }

    @Test
    void shouldCreateModifiedCopyUsingToBuilder() {
        Property original = Property.builder()
                .id(1L)
                .address("Calle 10 # 35-20, Medellin")
                .type(PropertyType.APARTMENT)
                .build();

        Property modified = original.toBuilder()
                .address("Calle 20 # 40-50, Medellin")
                .type(PropertyType.COMMERCIAL_SPACE)
                .build();

        assertNotSame(original, modified);

        assertEquals(original.id(), modified.id());

        assertEquals(
                "Calle 10 # 35-20, Medellin",
                original.address()
        );
        assertEquals(
                PropertyType.APARTMENT,
                original.type()
        );

        assertEquals(
                "Calle 20 # 40-50, Medellin",
                modified.address()
        );
        assertEquals(
                PropertyType.COMMERCIAL_SPACE,
                modified.type()
        );
    }
}