package pizzahub.api.entities.menuitem.data;

import pizzahub.api.entities.ingredient.data.IngredientResponse;

import java.math.BigDecimal;
import java.util.List;

public record MenuItemResponse(
    String slug,
    String name,
    BigDecimal price,
    List<IngredientResponse> ingredients
) {}
