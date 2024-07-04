package pizzahub.api.entities.menuitem.data;

import java.math.BigDecimal;
import java.util.Map;

public record MenuItemResponse(
    Long id,
    BigDecimal price,
    String name,
    Map<Long, String> ingredients
) {}
