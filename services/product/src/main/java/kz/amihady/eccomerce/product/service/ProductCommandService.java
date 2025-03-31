package kz.amihady.eccomerce.product.service;

import kz.amihady.eccomerce.product.request.CreateRequest;
import kz.amihady.eccomerce.product.request.UpdateRequest;
import kz.amihady.eccomerce.product.response.ProductUpdateResponse;

import java.util.UUID;

public interface ProductCommandService {

    UUID create(CreateRequest request);
    void delete(UUID id);
    ProductUpdateResponse update(UUID id, UpdateRequest request);
}
