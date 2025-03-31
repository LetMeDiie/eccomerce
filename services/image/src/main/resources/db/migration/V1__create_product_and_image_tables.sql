CREATE TABLE product (
    id UUID PRIMARY KEY
);

CREATE TABLE image (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL ,
    status VARCHAR(50) NOT NULL
);
