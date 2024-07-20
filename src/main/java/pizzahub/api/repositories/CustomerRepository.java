package pizzahub.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pizzahub.api.entities.user.customer.Customer;

public interface CustomerRepository extends JpaRepository <Customer, Long> {
    Optional<Customer> findByCPF(String cpf);
}
