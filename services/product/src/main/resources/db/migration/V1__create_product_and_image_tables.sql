

CREATE TABLE product (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(19, 2) NOT NULL,
    in_stock BIGINT NOT NULL
);

CREATE TABLE image (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    CONSTRAINT fk_image_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);
