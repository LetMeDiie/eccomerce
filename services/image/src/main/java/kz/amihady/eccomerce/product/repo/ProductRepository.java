package kz.amihady.eccomerce.product.repo;

import kz.amihady.eccomerce.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
