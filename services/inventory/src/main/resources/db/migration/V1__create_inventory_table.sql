CREATE TABLE inventory (
    id BIGSERIAL PRIMARY KEY,
    product_id UUID NOT NULL UNIQUE,
    in_stock BIGINT NOT NULL,
    reserved BIGINT NOT NULL
);