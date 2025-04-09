package kz.amihady.eccomerce.order.repo;

import kz.amihady.eccomerce.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByCustomerId(UUID customerId);

    Optional<Order> findByIdAndCustomerId(UUID orderId, UUID customerId);
}
