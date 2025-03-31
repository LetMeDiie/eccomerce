package kz.amihady.eccomerce.image.repo;

import kz.amihady.eccomerce.image.Status;
import kz.amihady.eccomerce.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    List<Image> findByStatus(Status status);

    List<Image> findByProductId(UUID productId);
}
