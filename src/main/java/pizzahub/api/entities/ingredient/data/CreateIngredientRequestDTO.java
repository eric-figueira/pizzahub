package pizzahub.api.entities.ingredient.data;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateIngredientRequestDTO(
    @NotBlank
    String name,

    @NotNull
    List<Long> menuItemsIds
) {}
