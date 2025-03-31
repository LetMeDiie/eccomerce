package kz.amihady.eccomerce.product.service;

import kz.amihady.eccomerce.product.entity.Product;

import java.util.UUID;

public interface ProductService {

    Product create(UUID id);
    void delete(UUID id);
    boolean existsById(UUID id);

}
