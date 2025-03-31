package kz.amihady.eccomerce.product.controller;

import jakarta.validation.Valid;
import kz.amihady.eccomerce.product.request.CreateRequest;
import kz.amihady.eccomerce.product.response.ProductResponse;
import kz.amihady.eccomerce.product.service.ProductCommandService;
import kz.amihady.eccomerce.product.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductsRestController {
    private final ProductQueryService queryService;
    private final ProductCommandService commandService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(
            @RequestParam(defaultValue = "1") Integer page) {
        return ResponseEntity.ok(queryService.findAll(page));
    }

    @PostMapping
    public ResponseEntity<UUID> create (
            @RequestBody @Valid CreateRequest request){
        return ResponseEntity.ok(commandService.create(request));
    }
}
