package pizzahub.api.entities.order.data;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import pizzahub.api.entities.order.OrderStatus;
import pizzahub.api.entities.order.PaymentMethod;

public record CreateOrderRequestDTO (
    @NotNull short number,
    @NotNull UUID clientId,

    @NotNull Date orderDate,
    @NotNull LocalTime orderTime,

    BigDecimal shippingTax,

    PaymentMethod paymentMethod,

    @NotNull OrderStatus orderStatus) {
}
