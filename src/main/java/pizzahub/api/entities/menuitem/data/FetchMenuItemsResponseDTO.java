package pizzahub.api.entities.menuitem.data;

import java.math.BigDecimal;
import java.util.List;

public record FetchMenuItemsResponseDTO (
    Long id,
    BigDecimal price,
    String name,
    List<String> ingredientNames
) { }
