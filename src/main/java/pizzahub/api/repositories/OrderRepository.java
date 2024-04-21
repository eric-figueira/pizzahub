package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pizzahub.api.entities.order.Order;

public interface OrderRepository extends JpaRepository <Order, Long> {}
