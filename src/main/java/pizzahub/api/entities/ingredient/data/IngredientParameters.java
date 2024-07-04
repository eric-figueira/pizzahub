package pizzahub.api.entities.ingredient.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IngredientParameters(
    @NotNull
    @NotBlank
    String name
) {}
