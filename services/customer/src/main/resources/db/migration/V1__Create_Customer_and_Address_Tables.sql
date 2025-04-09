
CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    house_number VARCHAR(50) NOT NULL,
    zip_code VARCHAR(20) NOT NULL
);

CREATE TABLE customer (
    id UUID PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    address_id BIGINT,
    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE
);