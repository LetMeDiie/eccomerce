package kz.amihady.eccomerce.inventory.controller;


import jakarta.validation.Valid;
import kz.amihady.eccomerce.inventory.request.InventoryUpdateRequest;
import kz.amihady.eccomerce.inventory.service.InventoryService;
import kz.amihady.eccomerce.order.request.OrderInventoryRequest;
import kz.amihady.eccomerce.order.service.OrderInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryRestController {
    private final InventoryService inventoryService;
    private final OrderInventoryService orderInventoryService;


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

    @PostMapping("/purchase")
    public ResponseEntity<Void> purchaseProduct(
            @RequestBody  OrderInventoryRequest request){
        orderInventoryService.purchase(request);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/purchase")
    public ResponseEntity<Void> cancelPurchase(
            @RequestBody OrderInventoryRequest request){
        orderInventoryService.cancelPurchase(request);
        return ResponseEntity.status(204).build();
    }
}
