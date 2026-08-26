CREATE TABLE properties (
                            id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                            address VARCHAR(255) NOT NULL,
                            type VARCHAR(30) NOT NULL,

                            CONSTRAINT chk_properties_type
                                CHECK (type IN ('HOUSE', 'APARTMENT', 'COMMERCIAL_SPACE'))
);


CREATE TABLE contracts (
                           id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                           code VARCHAR(50) NOT NULL,
                           status VARCHAR(20) NOT NULL,
                           property_id BIGINT UNSIGNED NOT NULL,

                           CONSTRAINT uk_contracts_code
                               UNIQUE (code),

                           CONSTRAINT chk_contracts_status
                               CHECK (status IN ('ACTIVE', 'INACTIVE')),

                           CONSTRAINT fk_contracts_property
                               FOREIGN KEY (property_id)
                                   REFERENCES properties(id)
                                   ON DELETE RESTRICT
);


CREATE TABLE persons (
                         id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                         first_name VARCHAR(100) NOT NULL,
                         last_name VARCHAR(100) NOT NULL,
                         identity_document VARCHAR(50) NOT NULL,
                         email VARCHAR(150) NOT NULL
);


CREATE TABLE contract_parties (
                                  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                  contract_id BIGINT UNSIGNED NOT NULL,
                                  person_id BIGINT UNSIGNED NOT NULL,
                                  role VARCHAR(20) NOT NULL,

                                  CONSTRAINT chk_contract_parties_role
                                      CHECK (role IN ('TENANT', 'OWNER', 'GUARANTOR')),

                                  CONSTRAINT uk_contract_parties_contract_person
                                      UNIQUE (contract_id, person_id),

                                  CONSTRAINT fk_contract_parties_contract
                                      FOREIGN KEY (contract_id)
                                          REFERENCES contracts(id)
                                          ON DELETE RESTRICT,

                                  CONSTRAINT fk_contract_parties_person
                                      FOREIGN KEY (person_id)
                                          REFERENCES persons(id)
                                          ON DELETE RESTRICT
);


CREATE INDEX idx_contracts_property_id
    ON contracts(property_id);

CREATE INDEX idx_contracts_status
    ON contracts(status);

CREATE INDEX idx_persons_identity_document
    ON persons(identity_document);

CREATE INDEX idx_persons_email
    ON persons(email);

CREATE INDEX idx_contract_parties_contract_id
    ON contract_parties(contract_id);

CREATE INDEX idx_contract_parties_person_id
    ON contract_parties(person_id);

CREATE INDEX idx_contract_parties_role
    ON contract_parties(role);