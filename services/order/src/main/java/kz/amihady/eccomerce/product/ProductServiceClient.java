package kz.amihady.eccomerce.product;

import kz.amihady.eccomerce.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.UUID;


@FeignClient(
        name = "product-service",
        url = "${application.config.product-url}",
        configuration = FeignConfig.class
)
public interface ProductServiceClient {

    @GetMapping("/{productId}/price")
    BigDecimal getProductPrice(@PathVariable("productId") UUID productId);


}
