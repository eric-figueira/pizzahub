package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzahub.api.entities.Customer;

public interface CustomerRepository extends JpaRepository <Customer, Long> {}
