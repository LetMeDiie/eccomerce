package kz.amihady.eccomerce.minio.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import kz.amihady.eccomerce.minio.config.MinioProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioBucketService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;


    @PostConstruct
    public void init() {
        createBucketIfNotExists();
    }

    public void createBucketIfNotExists() {
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
                log.info("Бакет создан: " + minioProperties.getBucketName());
            }
        } catch (Exception e) {
            log.error("Ошибка создания бакета");
        }
    }
}
