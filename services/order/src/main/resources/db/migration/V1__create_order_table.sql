

CREATE TABLE orders (
    id UUID PRIMARY KEY ,
    customer_id UUID NOT NULL,
    product_id UUID NOT NULL,
    order_status VARCHAR(255) NOT NULL,
    total_price DECIMAL(19, 4) NOT NULL,
    quantity BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL
);