package pizzahub.api.entities.order.data;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import pizzahub.api.entities.order.OrderStatus;
import pizzahub.api.entities.order.PaymentMethod;

public record UpdateOrderParameters(
    @NotNull
    Short number,

    @NotNull
    Long customerId,

    @NotNull
    BigDecimal cost,

    @NotNull
    BigDecimal shippingTax,

    @NotNull
    PaymentMethod paymentMethod,

    @NotNull
    OrderStatus orderStatus,

    @NotNull
    List<Long> menuItemsIds
) {}
