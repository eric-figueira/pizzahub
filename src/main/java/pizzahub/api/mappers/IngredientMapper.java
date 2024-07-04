package pizzahub.api.mappers;

import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.ingredient.data.IngredientResponse;

public class IngredientMapper {
    public static IngredientResponse modelToResponse(Ingredient model) {
        return new IngredientResponse(
            model.getId(),
            model.getName()
        );
    }
}
