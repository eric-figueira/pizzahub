package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzahub.api.entities.Pizzaria;

public interface PizzariaRepository extends JpaRepository <Pizzaria, Long> {}