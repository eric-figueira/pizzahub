package pizzahub.api.entities.order.data;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

import pizzahub.api.entities.order.OrderStatus;
import pizzahub.api.entities.order.PaymentMethod;

public record UpdateOrderRequestDTO (
    short number,
    Long customerId,

    Date orderDate,
    LocalTime orderTime,

    BigDecimal shippingTax,

    PaymentMethod paymentMethod,

    OrderStatus orderStatus
) {}
