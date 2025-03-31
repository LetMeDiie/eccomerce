package kz.amihady.eccomerce.image.service;


import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.image.entity.Image;
import kz.amihady.eccomerce.image.repo.ImageRepository;
import kz.amihady.eccomerce.image.request.ImageRequest;
import kz.amihady.eccomerce.product.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepository repository;
    private final ProductQueryService productQueryService;

    @Transactional
    public void addImage(ImageRequest request){
        log.info("Начало добавления изображения: productId={}, imageUrl={}", request.productId(), request.imageUrl());

        var product = productQueryService.findById(request.productId());

        Image image = new Image(request.imageId() , product , request.imageUrl());
        repository.save(image);

        log.info("Изображение с ID {} успешно добавлено к продукту с ID {}", request.imageId(), product.getId());

        productQueryService.deleteProductFromRedis(request.productId());
    }

    @Transactional
    public void deleteImage(UUID id) {
        log.info("Попытка удаления изображения с ID: {}", id);

        var image = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Изображение с ID {} не найдено", id);
                    return new EntityNotFoundException("Изображение не найдено");
                });

        repository.delete(image);
        log.info("Изображение с ID {} успешно удалено", id);

        productQueryService.deleteProductFromRedis(image.getProduct().getId());
    }
}
