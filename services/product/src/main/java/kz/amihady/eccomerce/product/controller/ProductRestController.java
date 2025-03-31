package kz.amihady.eccomerce.product.controller;


import jakarta.validation.Valid;
import kz.amihady.eccomerce.product.entity.Product;
import kz.amihady.eccomerce.product.request.UpdateRequest;
import kz.amihady.eccomerce.product.response.ProductResponse;
import kz.amihady.eccomerce.product.response.ProductUpdateResponse;
import kz.amihady.eccomerce.product.service.ProductCommandService;
import kz.amihady.eccomerce.product.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}")
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductCommandService commandService;
    private final ProductQueryService queryService;


    @GetMapping
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(queryService.findProduct(productId));
    }

    @PutMapping
    public ResponseEntity<ProductUpdateResponse> updateProduct(
            @PathVariable UUID productId,
            @RequestBody @Valid UpdateRequest request){
        return ResponseEntity.ok(commandService.update(productId,request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        commandService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
