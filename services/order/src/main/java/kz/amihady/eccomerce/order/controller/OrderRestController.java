package kz.amihady.eccomerce.order.controller;

import jakarta.validation.Valid;
import kz.amihady.eccomerce.order.request.OrderRequest;
import kz.amihady.eccomerce.order.response.OrderResponse;
import kz.amihady.eccomerce.order.OrderCommandService;
import kz.amihady.eccomerce.order.OrderQueryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/api/orders")
public class OrderRestController {
    OrderCommandService orderCommandService;
    OrderQueryService orderQueryService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> findAll(
            @PathVariable("customerId") UUID customerId) {
        return ResponseEntity
                .ok(orderQueryService.findAll(customerId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> findById(
            @PathVariable("orderId") UUID orderId,
            @RequestParam("customerId") UUID customerId) {  // Предполагаем, что customerId передается как query-параметр

        return ResponseEntity.ok(orderQueryService.findById(orderId, customerId));
    }


    @PostMapping
    public ResponseEntity<UUID> createOrder(
            @RequestBody @Valid OrderRequest request){
        return ResponseEntity
                .ok(orderCommandService.createOrder(request));
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(
            @PathVariable("orderId") UUID orderId){
        orderCommandService.cancelOrder(orderId);
        return ResponseEntity
                .ok("Заказ успешно отменен.");
    }




}
