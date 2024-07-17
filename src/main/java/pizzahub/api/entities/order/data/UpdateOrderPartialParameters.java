package pizzahub.api.entities.order.data;

import jakarta.validation.constraints.NotNull;
import pizzahub.api.entities.order.OrderStatus;
import pizzahub.api.entities.order.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record UpdateOrderPartialParameters(
    Short number,
    Long customerId,
    BigDecimal cost,
    BigDecimal shippingTax,
    PaymentMethod paymentMethod,
    OrderStatus orderStatus,
    List<Long> ingredientsIds
) {}