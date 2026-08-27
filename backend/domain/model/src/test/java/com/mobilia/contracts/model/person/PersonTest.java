package com.mobilia.contracts.model.person;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class PersonTest {

    @Test
    void shouldCreatePersonWithExpectedValues() {
        Person person = new Person(
                1L,
                "Ana",
                "Torres",
                "1035001003",
                "ana.torres@example.com"
        );

        assertEquals(1L, person.id());
        assertEquals("Ana", person.firstName());
        assertEquals("Torres", person.lastName());
        assertEquals("1035001003", person.identityDocument());
        assertEquals("ana.torres@example.com", person.email());
    }

    @Test
    void shouldCreatePersonUsingBuilder() {
        Person person = Person.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Torres")
                .identityDocument("1035001003")
                .email("ana.torres@example.com")
                .build();

        assertEquals(1L, person.id());
        assertEquals("Ana", person.firstName());
        assertEquals("Torres", person.lastName());
        assertEquals("1035001003", person.identityDocument());
        assertEquals("ana.torres@example.com", person.email());
    }

    @Test
    void shouldReturnFullName() {
        Person person = new Person(
                1L,
                "Ana",
                "Torres",
                "1035001003",
                "ana.torres@example.com"
        );

        String fullName = person.fullName();

        assertEquals("Ana Torres", fullName);
    }

    @Test
    void shouldCreateModifiedCopyUsingToBuilder() {
        Person original = Person.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Torres")
                .identityDocument("1035001003")
                .email("ana.torres@example.com")
                .build();

        Person modified = original.toBuilder()
                .email("ana.torres@mobilia.com")
                .build();

        assertNotSame(original, modified);

        assertEquals(original.id(), modified.id());
        assertEquals(original.firstName(), modified.firstName());
        assertEquals(original.lastName(), modified.lastName());
        assertEquals(original.identityDocument(), modified.identityDocument());

        assertEquals("ana.torres@example.com", original.email());
        assertEquals("ana.torres@mobilia.com", modified.email());
    }
}