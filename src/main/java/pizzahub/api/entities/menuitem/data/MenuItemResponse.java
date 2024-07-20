package pizzahub.api.entities.menuitem.data;

import pizzahub.api.entities.ingredient.Ingredient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record MenuItemResponse(
    Long id,
    BigDecimal price,
    String name,
    List<Ingredient> ingredients
) {}
