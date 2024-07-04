package pizzahub.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pizzahub.api.entities.pizzeria.Pizzeria;

public interface PizzeriaRepository extends JpaRepository <Pizzeria, Long> {
    Optional<Pizzeria> findByCode(Short code);
}
