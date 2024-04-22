package pizzahub.api.entities.ingredient;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import pizzahub.api.entities.menuitem.MenuItem;

public record IngredientRequestDTO(
    @NotBlank
    String name,

    @Nullable
    List<MenuItem> menuItems
) {}
