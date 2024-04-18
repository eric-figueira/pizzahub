package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzahub.api.entities.Order;

public interface OrderRepository extends JpaRepository <Order, Long> {}
