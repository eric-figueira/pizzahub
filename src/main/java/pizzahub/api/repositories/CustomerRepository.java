package pizzahub.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzahub.api.entities.Customer;

public interface CustomerRepository extends JpaRepository <Customer, UUID> {}
