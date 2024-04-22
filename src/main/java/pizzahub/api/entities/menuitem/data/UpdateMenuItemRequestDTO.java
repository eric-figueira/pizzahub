package pizzahub.api.entities.menuitem.data;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public record UpdateMenuItemRequestDTO (
    @NotNull
    Long id,

    String name,
    BigDecimal price,
    List<Long> ingredientsIds
) {}

