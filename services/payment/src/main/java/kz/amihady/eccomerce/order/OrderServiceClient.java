package kz.amihady.eccomerce.order;

import kz.amihady.eccomerce.config.FeignConfig;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "order-service",
        url = "${application.config.order-url}",
        configuration = FeignConfig.class
)
public interface OrderServiceClient {

    @GetMapping("/{orderId}/{customerId}")
    OrderResponse findById(@PathVariable("orderId") UUID orderId,
                           @PathVariable("customerId") UUID customerId);
}
