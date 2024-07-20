package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pizzahub.api.entities.ingredient.Ingredient;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository <Ingredient, Long> {
    Optional<Ingredient> findBySlug(String slug);
}
