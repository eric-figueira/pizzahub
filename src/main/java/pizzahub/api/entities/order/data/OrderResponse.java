package pizzahub.api.entities.order.data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

import pizzahub.api.entities.order.OrderStatus;
import pizzahub.api.entities.order.PaymentMethod;
import pizzahub.api.entities.user.customer.Customer;

public record OrderResponse(
    Long id,
    Short number,
    Customer customer,
    Date orderDate,
    LocalTime orderTime,
    BigDecimal cost,
    BigDecimal shippingTax,
    PaymentMethod paymentMethod,
    OrderStatus orderStatus,
    Map<Long, String> menuItems
) {}
