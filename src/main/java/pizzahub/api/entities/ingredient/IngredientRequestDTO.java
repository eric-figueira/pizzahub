package pizzahub.api.entities.ingredient;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record IngredientRequestDTO(
    @NotBlank
    String name,

    @Nullable
    List<Long> menuItemsIds
) {}
