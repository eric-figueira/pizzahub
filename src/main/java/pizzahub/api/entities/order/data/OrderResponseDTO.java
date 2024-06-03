package pizzahub.api.entities.order.data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

import pizzahub.api.entities.order.OrderStatus;
import pizzahub.api.entities.order.PaymentMethod;
import pizzahub.api.entities.user.customer.Customer;

public record OrderResponseDTO(
    Long id,
    Short number,
    Customer customer,
    Date orderDate,
    LocalTime orderTime,
    BigDecimal shippingTax,
    PaymentMethod paymentMethod,
    OrderStatus orderStatus
) { }
