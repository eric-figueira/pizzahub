package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pizzahub.api.entities.pizzeria.Pizzeria;

public interface PizzeriaRepository extends JpaRepository <Pizzeria, Short> {}
