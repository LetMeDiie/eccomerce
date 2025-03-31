package kz.amihady.eccomerce.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageService {
    UUID addImage(UUID productId , MultipartFile file);

    void deleteImage(UUID id);

    void deleteImagesForProduct(UUID productId);



}
