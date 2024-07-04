package pizzahub.api.mappers;

import pizzahub.api.entities.order.Order;
import pizzahub.api.entities.order.data.OrderResponse;

public class OrderMapper {
    public static OrderResponse modelToResponse(Order model) {
        return new OrderResponse(
            model.getId(),
            model.getNumber(),
            model.getCustomer(),
            model.getOrderDate(),
            model.getOrderTime(),
            model.getCost(),
            model.getShippingTax(),
            model.getPaymentMethod(),
            model.getOrderStatus()
        );
    }
}
