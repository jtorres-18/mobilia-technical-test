-- =========================================================
-- PROPERTIES
-- =========================================================

INSERT INTO properties (id, address, type) VALUES
                                               (1, 'Calle 10 # 35-20, Medellin', 'APARTMENT'),
                                               (2, 'Carrera 70 # 45-18, Medellin', 'HOUSE'),
                                               (3, 'Calle 33 # 80-15, Medellin', 'COMMERCIAL_SPACE'),
                                               (4, 'Carrera 43A # 1-50, Medellin', 'APARTMENT');


-- =========================================================
-- PERSONS
-- =========================================================

INSERT INTO persons (
    id,
    first_name,
    last_name,
    identity_document,
    email
) VALUES
      (1, 'Laura', 'Gomez', '1035001001', 'laura.gomez@example.com'),
      (2, 'Carlos', 'Restrepo', '1035001002', 'carlos.restrepo@example.com'),
      (3, 'Ana', 'Torres', '1035001003', 'ana.torres@example.com'),
      (4, 'Mateo', 'Ruiz', '1035001004', 'mateo.ruiz@example.com'),
      (5, 'Sofia', 'Herrera', '1035001005', 'sofia.herrera@example.com'),
      (6, 'Daniel', 'Castro', '1035001006', 'daniel.castro@example.com'),
      (7, 'Valentina', 'Marin', '1035001007', 'valentina.marin@example.com'),
      (8, 'Miguel', 'Rojas', '1035001008', 'miguel.rojas@example.com'),
      (9, 'Juliana', 'Velez', '1035001009', 'juliana.velez@example.com');


-- =========================================================
-- CONTRACTS
-- =========================================================


INSERT INTO contracts (id, code, status, property_id) VALUES
                                                          (1, 'MBL-A100', 'ACTIVE', 1),
                                                          (2, 'MBL-A090', 'INACTIVE', 1),
                                                          (3, 'MBL-C200', 'ACTIVE', 2),
                                                          (4, 'MBL-L300', 'INACTIVE', 3),
                                                          (5, 'MBL-L301', 'ACTIVE', 3),
                                                          (6, 'MBL-A400', 'ACTIVE', 4);

-- =========================================================
-- CONTRACT PARTIES
-- =========================================================


INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (1, 1, 'TENANT'),
                                                                (1, 2, 'OWNER'),
                                                                (1, 3, 'OWNER'),
                                                                (1, 5, 'GUARANTOR');

-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (2, 4, 'TENANT'),
                                                                (2, 2, 'OWNER');

-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (3, 6, 'TENANT'),
                                                                (3, 7, 'OWNER'),
                                                                (3, 3, 'GUARANTOR');
-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (4, 8, 'TENANT'),
                                                                (4, 9, 'OWNER'),
                                                                (4, 5, 'GUARANTOR');

-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (5, 1, 'TENANT'),
                                                                (5, 9, 'OWNER'),
                                                                (5, 6, 'GUARANTOR');


-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (6, 7, 'TENANT'),
                                                                (6, 4, 'OWNER');