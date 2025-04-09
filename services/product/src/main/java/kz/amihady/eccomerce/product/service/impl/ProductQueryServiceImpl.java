package kz.amihady.eccomerce.product.service.impl;


import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.product.entity.Product;
import kz.amihady.eccomerce.product.mapper.ProductMapper;
import kz.amihady.eccomerce.product.repo.ProductRepository;
import kz.amihady.eccomerce.product.response.ProductResponse;
import kz.amihady.eccomerce.product.service.ProductQueryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ProductQueryServiceImpl implements ProductQueryService {
    ProductRepository productRepository;
    ProductMapper productMapper;

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponse findProduct(UUID id) {
        log.info("Запрос на получение информации о продукте с ID: {}", id);
        var product = findById(id);
        var response = productMapper.fromProduct(product);
        log.info("Продукт с ID {} успешно найден и преобразован", id);
        return response;
    }

    @Override
    public List<ProductResponse> findAll(Integer page) {
        page=Math.max(page,1)-1;
        log.info("Получение списка продуктов. Запрос: page={}", page+1);

        PageRequest pageRequest = PageRequest.of(page, 20);

        Page<Product> productPage = productRepository.findAll(pageRequest);

        if (productPage.isEmpty()) {
            log.info("На запрашиваемой странице {} нет продуктов.", page);
            return Collections.emptyList();
        }

        return productPage.getContent().stream()
                .map(productMapper::fromProduct)
                .toList();
    }

    @Override
    public BigDecimal getProductPrice(UUID id) {
        return findById(id).getPrice();
    }

    @Override
    public Product findById(UUID id) throws EntityNotFoundException {
        log.info("Поиск продукта по ID: {}", id);
        return productRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.warn("Продукт с ID {} не найден", id);
                    return new EntityNotFoundException("Продукт не найден");
                });
    }


    @Override
    @CacheEvict(value = "products", key = "#id")
    @Transactional
    public void deleteProductFromRedis(UUID id){
        log.info("Удаляем продукт из кеша");
    }
}
