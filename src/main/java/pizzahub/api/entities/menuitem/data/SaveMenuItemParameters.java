package pizzahub.api.entities.menuitem.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record SaveMenuItemParameters(
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 5, max = 35, message = "Name must contain at least 5 and a max of 35 characters")
    String name,

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price cannot be equal or lower than zero")
    @DecimalMax(value = "250.0", inclusive = true, message = "Price cannot be grater than 250.00")
    BigDecimal price,

    @NotEmpty(message = "Ingredients IDs list cannot be empty")
    @NotNull(message = "Ingredients IDs list cannot be null")
    @JsonProperty("ingredients_ids")
    List<Long> ingredientsIds
) {}
