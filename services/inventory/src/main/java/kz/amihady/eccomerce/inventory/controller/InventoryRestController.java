package kz.amihady.eccomerce.inventory.controller;


import jakarta.validation.Valid;
import kz.amihady.eccomerce.inventory.request.InventoryUpdateRequest;
import kz.amihady.eccomerce.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryRestController {
    private final InventoryService inventoryService;


    @GetMapping("/{productId}")
    public ResponseEntity<Long> getProductQuantity(
            @PathVariable("productId")UUID productId){
        return ResponseEntity.ok(inventoryService.getProductQuantity(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Long> updateProductQuantity(
            @PathVariable("productId") UUID productId,
            @RequestBody @Valid InventoryUpdateRequest request){
        return ResponseEntity.ok(
                inventoryService.updateProductQuantity(productId,request));
    }
}
