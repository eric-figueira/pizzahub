package pizzahub.api.entities.ingredient.data;

import java.util.List;

public record FetchIngredientsResponseDTO (
    Long id,
    String name,
    List<String> menuItems
) {}
