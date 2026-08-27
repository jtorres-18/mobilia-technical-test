package com.mobilia.contracts;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BackendApplicationTest {

    @Test
    void shouldRunSpringApplication() {
        String[] args = {};

        try (
                MockedStatic<SpringApplication> springApplicationMock = Mockito.mockStatic(SpringApplication.class)
        ) {
            BackendApplication.main(args);
            springApplicationMock.verify(() -> SpringApplication.run(BackendApplication.class, args));
        }
    }

    @Test
    void shouldCreateBackendApplicationInstance() {
        BackendApplication application = new BackendApplication();
        assertNotNull(application);
    }
}