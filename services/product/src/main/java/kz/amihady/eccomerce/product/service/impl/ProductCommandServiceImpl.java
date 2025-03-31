package kz.amihady.eccomerce.product.service.impl;

import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.kafka.product.ProductKafkaProducer;
import kz.amihady.eccomerce.kafka.product.event.ProductCreatedEvent;
import kz.amihady.eccomerce.kafka.product.event.ProductDeletedEvent;
import kz.amihady.eccomerce.product.mapper.ProductMapper;
import kz.amihady.eccomerce.product.repo.ProductRepository;
import kz.amihady.eccomerce.product.request.CreateRequest;
import kz.amihady.eccomerce.product.request.UpdateRequest;
import kz.amihady.eccomerce.product.response.ProductUpdateResponse;
import kz.amihady.eccomerce.product.service.ProductCommandService;
import kz.amihady.eccomerce.product.service.ProductUpdater;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ProductCommandServiceImpl implements ProductCommandService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    ProductUpdater productUpdater;
    ProductKafkaProducer productKafkaProducer;



    @Override
    public UUID create(CreateRequest request) {
        log.info("Запрос на создание продукта");

        var product = productMapper.toProduct(request);
        productRepository.save(product);

        log.info("Продукт  создан и сохранен в БД с ID: {}", product.getId());

        //отправляем событие о создание продуктам.
        ProductCreatedEvent event = new ProductCreatedEvent(product.getId(),request.quantity());
        productKafkaProducer.sendProductCreatedEvent(event);


        return product.getId();
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void delete(UUID id) {
        log.info("Запрос на удаление продукта с ID: {}", id);

        var product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Продукт с ID {} не найден", id);
                    return new EntityNotFoundException("Продукт не найден.");
                });

        productRepository.delete(product);
        log.info("Продукт с ID {} успешно удалён из БД", id);

        // отправить событие об удалении продукта
        ProductDeletedEvent event = new ProductDeletedEvent(id);
        productKafkaProducer.sendProductDeletedEvent(event);
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public ProductUpdateResponse update(UUID id, UpdateRequest request) {
        var product = productRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Продукт не найден"));
        return productUpdater.updateProduct(request,product);
    }



}
