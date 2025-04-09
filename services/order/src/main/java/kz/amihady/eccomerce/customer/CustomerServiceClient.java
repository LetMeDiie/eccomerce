package kz.amihady.eccomerce.customer;

import kz.amihady.eccomerce.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-url}",
        configuration = FeignConfig.class
)
public interface CustomerServiceClient {
    @GetMapping("/{customerId}")
    CustomerResponse findById(@PathVariable("customerId") UUID customerId);
}
