package kz.amihady.eccomerce.customer.controller;

import jakarta.validation.Valid;
import kz.amihady.eccomerce.customer.request.CustomerRequest;
import kz.amihady.eccomerce.customer.response.AddressResponse;
import kz.amihady.eccomerce.customer.response.CustomerResponse;
import kz.amihady.eccomerce.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerRestController {
    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> findById(
            @PathVariable("customerId")UUID customerId){
        return ResponseEntity.ok(customerService.findById(customerId));
    }

    @GetMapping("/address/{customerId}")
    public ResponseEntity<AddressResponse> findAddressById(
            @PathVariable("customerId") UUID customerId){
        return ResponseEntity
                .ok(customerService.findAddress(customerId));
    }


    @PostMapping
    public  ResponseEntity<UUID> createCustomer(
             @RequestBody @Valid CustomerRequest request){
        return ResponseEntity.ok(customerService.createCustomer(request));
    }
}
