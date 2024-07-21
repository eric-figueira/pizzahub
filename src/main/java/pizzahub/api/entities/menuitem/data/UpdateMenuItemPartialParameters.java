package pizzahub.api.entities.menuitem.data;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record UpdateMenuItemPartialParameters(
    @NotBlank(message = "Slug cannot be empty")
    @Size(min = 5, max = 40, message = "Slug must contain at least 3 and a max of 30 characters")
    String slug,

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 5, max = 40, message = "Name must contain at least 5 and a max of 35 characters")
    String name,

    @DecimalMin(value = "0.0", inclusive = false, message = "Price cannot be equal or lower than zero")
    @DecimalMax(value = "250.0", inclusive = true, message = "Price cannot be grater than 250.00")
    BigDecimal price,

    @NotEmpty(message = "Ingredients IDs list cannot be empty")
    @JsonProperty("ingredients_ids")
    List<Long> ingredientsIds
) {}

