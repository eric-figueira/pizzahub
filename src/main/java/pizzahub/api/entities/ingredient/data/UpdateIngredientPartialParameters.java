package pizzahub.api.entities.ingredient.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateIngredientPartialParameters(
    @NotBlank(message = "Slug cannot be empty")
    @Size(min = 3, max = 35, message = "Slug must contain at least 3 and a max of 30 characters")
    String slug,

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 5, max = 35, message = "Name must contain at least 5 and a max of 35 characters")
    String name
) {}
