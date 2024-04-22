package pizzahub.api.entities.menuitem.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record CreateMenuItemRequestDTO (
    @NotBlank
    String name,

    @NotNull
    BigDecimal price,

    @NotNull
    List<Long> ingredientsIds
) {}
