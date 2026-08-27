package com.mobilia.contracts.model.property;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertyTypeTest {

    @Test
    void shouldContainExpectedPropertyTypes() {
        PropertyType[] expectedTypes = {
                PropertyType.HOUSE,
                PropertyType.APARTMENT,
                PropertyType.COMMERCIAL_SPACE
        };

        assertArrayEquals(
                expectedTypes,
                PropertyType.values()
        );
    }

    @Test
    void shouldReturnHouseTypeByName() {
        PropertyType type = PropertyType.valueOf("HOUSE");

        assertEquals(
                PropertyType.HOUSE,
                type
        );
    }

    @Test
    void shouldReturnApartmentTypeByName() {
        PropertyType type = PropertyType.valueOf("APARTMENT");

        assertEquals(
                PropertyType.APARTMENT,
                type
        );
    }

    @Test
    void shouldReturnCommercialSpaceTypeByName() {
        PropertyType type =
                PropertyType.valueOf("COMMERCIAL_SPACE");

        assertEquals(
                PropertyType.COMMERCIAL_SPACE,
                type
        );
    }
}