package kz.amihady.eccomerce.inventory.repo;

import kz.amihady.eccomerce.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(UUID productId);
    boolean existsByProductId(UUID productId);
}
