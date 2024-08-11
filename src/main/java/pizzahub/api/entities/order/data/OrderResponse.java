package pizzahub.api.entities.order.data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import pizzahub.api.entities.menuitem.data.MenuItemResponse;
import pizzahub.api.entities.order.OrderStatus;
import pizzahub.api.entities.order.PaymentMethod;
import pizzahub.api.entities.user.customer.data.CustomerResponse;

public record OrderResponse(
    Short number,
    Date orderDate,
    LocalTime orderTime,
    BigDecimal cost,
    BigDecimal shippingTax,
    PaymentMethod paymentMethod,
    OrderStatus orderStatus,
    List<MenuItemResponse> menuItems,
    CustomerResponse customer
) {}
