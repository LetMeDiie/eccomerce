package kz.amihady.eccomerce.product.service.impl;

import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.product.entity.Product;
import kz.amihady.eccomerce.product.repo.ProductRepository;
import kz.amihady.eccomerce.product.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository repository;

    @Override
    public Product create(UUID id) {
        log.info("Создание продукта с ID: {}", id);
        Product savedProduct = repository.save(new Product(id));
        log.info("Продукт успешно создан и сохранен в базе данных: {}", savedProduct);
        return savedProduct;
    }

    @Override
    public void delete(UUID id) {
        if(!existsById(id)) {
            log.error("Нет продукта с id:"+id.toString());
            throw new EntityNotFoundException("Нет продукта с id:"+id.toString());
        }
        log.info("Удаление продукта с ID: {}", id);
        repository.deleteById(id);
        log.info("Продукт с ID {} успешно удален", id);
    }

    @Override
    public boolean existsById(UUID id) {
        log.info("Проверка существования продукта с ID: {}", id);
        boolean exists = repository.existsById(id);
        log.info("Результат проверки: {}", exists ? "существует" : "не существует");
        return exists;
    }
}
