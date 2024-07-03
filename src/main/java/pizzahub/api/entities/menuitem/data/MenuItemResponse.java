package pizzahub.api.entities.menuitem.data;

import java.math.BigDecimal;
import java.util.List;

public record MenuItemResponse(
    Long id,
    BigDecimal price,
    String name,
    List<String> ingredientNames
) { }
