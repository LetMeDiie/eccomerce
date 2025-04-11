package kz.amihady.eccomerce.payment.controller;


import kz.amihady.eccomerce.payment.request.PaymentRequest;
import kz.amihady.eccomerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentRestController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> pay (@RequestBody PaymentRequest request){
        return ResponseEntity.ok(paymentService.createPayment(request));
    }
}
