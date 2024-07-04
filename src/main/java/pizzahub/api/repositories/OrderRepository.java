package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pizzahub.api.entities.order.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository <Order, Long> {
    Optional<Order> findByOrderNumber(Short number);
}
