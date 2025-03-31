package kz.amihady.eccomerce.product.service;


import kz.amihady.eccomerce.product.entity.Product;
import kz.amihady.eccomerce.product.request.UpdateRequest;
import kz.amihady.eccomerce.product.response.ProductUpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductUpdater {

//система сама обновит после завершения транзакции , вызывать save не нужно.
    public ProductUpdateResponse updateProduct(UpdateRequest request, Product product) {
        log.info("Обновление продукта с ID: {}", product.getId());

        if (request.description() != null) {
            log.info("Обновление описания");
            product.setDescription(request.description());
        }

        if (request.name() != null) {
            log.info("Обновление названия");
            product.setName(request.name());
        }

        if (request.price() != null) {
            log.info("Обновление цены");
            product.setPrice(request.price());
        }

        log.info("Продукт с ID {} успешно обновлён", product.getId());

        return new ProductUpdateResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
