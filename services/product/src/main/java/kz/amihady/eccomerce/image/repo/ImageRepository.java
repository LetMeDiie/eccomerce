package kz.amihady.eccomerce.image.repo;

import kz.amihady.eccomerce.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
