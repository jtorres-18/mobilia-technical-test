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

-- Property 1:
-- One active contract + one historical inactive contract.
INSERT INTO contracts (id, code, status, property_id) VALUES
                                                          (1, 'MBL-A100', 'ACTIVE', 1),
                                                          (2, 'MBL-A090', 'INACTIVE', 1),

-- Property 2:
-- One active contract.
                                                          (3, 'MBL-C200', 'ACTIVE', 2),

-- Property 3:
-- One inactive historical contract + one active contract.
                                                          (4, 'MBL-L300', 'INACTIVE', 3),
                                                          (5, 'MBL-L301', 'ACTIVE', 3),

-- Property 4:
-- One active contract.
                                                          (6, 'MBL-A400', 'ACTIVE', 4);


-- =========================================================
-- CONTRACT PARTIES
-- =========================================================

-- ---------------------------------------------------------
-- CONTRACT 1 - MBL-A100
-- Property: Calle 10 # 35-20
--
-- Tenant: Laura Gomez
-- Owners: Carlos Restrepo, Ana Torres
-- Guarantor: Sofia Herrera
-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (1, 1, 'TENANT'),
                                                                (1, 2, 'OWNER'),
                                                                (1, 3, 'OWNER'),
                                                                (1, 5, 'GUARANTOR');


-- ---------------------------------------------------------
-- CONTRACT 2 - MBL-A090
-- Historical contract for the same property.
--
-- Tenant: Mateo Ruiz
-- Owner: Carlos Restrepo
-- No guarantor.
-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (2, 4, 'TENANT'),
                                                                (2, 2, 'OWNER');


-- ---------------------------------------------------------
-- CONTRACT 3 - MBL-C200
--
-- Tenant: Daniel Castro
-- Owner: Valentina Marin
-- Guarantor: Ana Torres
--
-- Ana Torres was OWNER in contract 1 and GUARANTOR here.
-- This validates that a person can have different roles
-- in different contracts.
-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (3, 6, 'TENANT'),
                                                                (3, 7, 'OWNER'),
                                                                (3, 3, 'GUARANTOR');


-- ---------------------------------------------------------
-- CONTRACT 4 - MBL-L300
-- Historical contract.
--
-- Tenant: Miguel Rojas
-- Owner: Juliana Velez
-- Guarantor: Sofia Herrera
-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (4, 8, 'TENANT'),
                                                                (4, 9, 'OWNER'),
                                                                (4, 5, 'GUARANTOR');


-- ---------------------------------------------------------
-- CONTRACT 5 - MBL-L301
-- Current contract for the same commercial property.
--
-- Tenant: Laura Gomez
-- Owner: Juliana Velez
-- Guarantor: Daniel Castro
-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (5, 1, 'TENANT'),
                                                                (5, 9, 'OWNER'),
                                                                (5, 6, 'GUARANTOR');


-- ---------------------------------------------------------
-- CONTRACT 6 - MBL-A400
--
-- Tenant: Valentina Marin
-- Owner: Mateo Ruiz
-- No guarantor.
--
-- Mateo was TENANT in contract 2 and OWNER here.
-- ---------------------------------------------------------

INSERT INTO contract_parties (contract_id, person_id, role) VALUES
                                                                (6, 7, 'TENANT'),
                                                                (6, 4, 'OWNER');