package kz.amihady.eccomerce.minio.service;


import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import kz.amihady.eccomerce.exception.MinioException;
import kz.amihady.eccomerce.minio.config.MinioProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
public class MinioImageService {
    MinioClient minioClient;
    MinioProperties minioProperties;



    public String uploadImage(MultipartFile file, UUID imageId) {
        try {

            log.info("Загружаем изображение в MinIO... ID: "+imageId);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(imageId.toString())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );


            String imageUrl =  minioProperties.getUrl() + "/" +
                    minioProperties.getBucketName() + "/" +
                    imageId.toString();

            log.info("Изображение успешно загружено в MinIO, URL: {}", imageUrl);
            return  imageUrl;
        } catch (Exception exception) {
            log.error("Ошибка при добавлении изображение в MinIO");
            throw new MinioException("Ошибка при добавлении изображение в MinIO");
        }
    }


    public void deleteImage(UUID imageId)  {
        try {

            log.debug("Удаляем изображение из MinIO... ID: "+imageId);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(imageId.toString())
                            .build()
            );
            log.info("Изображение успешно удалено из  MinIO, ID: {}", imageId);

        } catch (Exception exception) {
            log.error("Ошибка при удалении фото из MinIO");
            throw new MinioException("Ошибка при удалении фото из MinIO");
        }
    }
}

