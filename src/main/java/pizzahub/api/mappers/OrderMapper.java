package pizzahub.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pizzahub.api.entities.order.Order;
import pizzahub.api.entities.order.data.CreateOrderParameters;
import pizzahub.api.entities.order.data.OrderResponse;
import pizzahub.api.entities.order.data.UpdateOrderParameters;

import java.util.List;

@Mapper(componentModel = "spring", uses = { CustomerMapper.class, MenuItemMapper.class })
public interface OrderMapper {
    OrderResponse fromEntityToResponse(Order entity);
    List<OrderResponse> fromEntityListToResponseList(List<Order> list);

    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "orderTime", ignore = true)
    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    Order fromCreateParametersToEntity(CreateOrderParameters parameters);

    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "orderTime", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    Order fromUpdateParametersToEntity(UpdateOrderParameters parameters);

    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "orderTime", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    void updateOrder(@MappingTarget Order target, UpdateOrderParameters parameters);
}
