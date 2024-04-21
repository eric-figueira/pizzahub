package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pizzahub.api.entities.ingredient.Ingredient;

public interface IngredientRepository extends JpaRepository <Ingredient, Long> {}
