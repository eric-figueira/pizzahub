package pizzahub.api.entities.order.data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import pizzahub.api.entities.order.PaymentMethod;

public record CreateOrderParameters(
    @NotNull(message = "Number cannot be null")
    @Min(value = 100, message = "Number cannot be equal or lower than 100")
    @Max(value = 32700, message = "Number cannot be grater than 32700")
    Short number,

    @NotNull(message = "Customer ID cannot be null")
    @JsonProperty("customer_id")
    UUID customerId,

    @NotNull(message = "Cost cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost cannot be equal or lower than zero")
    @DecimalMax(value = "250.0", inclusive = true, message = "Cost cannot be grater than 250.00")
    BigDecimal cost,

    @DecimalMin(value = "0.0", inclusive = false, message = "Shipping Tax cannot be equal or lower than zero")
    @DecimalMax(value = "250.0", inclusive = true, message = "Shipping Tax cannot be grater than 250.00")
    @JsonProperty("shipping_tax")
    BigDecimal shippingTax,

    @JsonProperty("payment_method")
    PaymentMethod paymentMethod,

    @NotEmpty(message = "Menu Items Slugs list cannot be empty")
    @NotNull(message = "Menu Items Slugs list cannot be null")
    @JsonProperty("menu_items_slugs")
    List<String> menuItemsSlugs
) {}
