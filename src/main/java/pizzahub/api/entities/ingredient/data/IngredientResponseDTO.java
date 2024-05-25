package pizzahub.api.entities.ingredient.data;

import java.util.List;

public record IngredientResponseDTO(
    Long id,
    String name,
    List<String> menuItems
) {}
