package pizzahub.api.entities.menuitem;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import pizzahub.api.entities.ingredient.Ingredient;

import java.math.BigDecimal;
import java.util.List;

public record MenuItemRequestDTO (
    @NotBlank
    String name,

    @NotNull
    BigDecimal price,

    @Nullable
    List<Ingredient> ingredientsIds
) {}
