package kz.amihady.eccomerce.product.service;

import kz.amihady.eccomerce.product.entity.Product;
import kz.amihady.eccomerce.product.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductQueryService {

    ProductResponse findProduct(UUID id);

    List<ProductResponse> findAll(Integer page);

    Product findById(UUID id);

    void deleteProductFromRedis(UUID id);
}