package pizzahub.api.entities.order.data;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import pizzahub.api.entities.order.PaymentMethod;

public record CreateOrderParameters(
    @NotNull
    Short number,

    @NotNull
    Long customerId,

    @NotNull
    BigDecimal cost,

    BigDecimal shippingTax,
    PaymentMethod paymentMethod,

    @NotNull
    List<Long> menuItemsIds
) {}
