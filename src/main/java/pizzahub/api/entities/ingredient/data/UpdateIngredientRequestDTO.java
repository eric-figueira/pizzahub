package pizzahub.api.entities.ingredient.data;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record UpdateIngredientRequestDTO(
    @NotNull
    Long id,

    String name,
    List<Long> menuItemsIds
) {}
