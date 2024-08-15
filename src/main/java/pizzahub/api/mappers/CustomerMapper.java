package pizzahub.api.mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pizzahub.api.entities.order.Order;
import pizzahub.api.entities.user.customer.Customer;
import pizzahub.api.entities.user.customer.data.SaveCustomerParameters;
import pizzahub.api.entities.user.customer.data.SaveDeliveryCustomerParameters;
import pizzahub.api.entities.user.customer.data.CustomerResponse;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "orders", ignore = true)
    Customer fromCreateParametersToEntity(SaveCustomerParameters parameters);

    Customer fromCreateDeliveryParametersToEntity(SaveDeliveryCustomerParameters parameters);

    @Mapping(target = "numberOfOrders", expression = "java(entity.getOrders() != null ? entity.getOrders().size() : 0)")
    @Mapping(target = "ordersNumbers", expression = "java(getOrdersNumbers(entity))")
    CustomerResponse fromEntityToResponse(Customer entity);

    List<CustomerResponse> fromEntityListToResponseList(List<Customer> list);

    @Mapping(target = "orders", ignore = true)
    void updateCustomer(@MappingTarget Customer target, SaveCustomerParameters parameters);

    @Mapping(target = "orders", ignore = true)
    void updateCustomer(@MappingTarget Customer target, SaveDeliveryCustomerParameters parameters);

    default List<Short> getOrdersNumbers(Customer entity) {
        if (entity.getOrders() == null) {
            return Collections.emptyList();
        }
        return entity.getOrders().stream()
            .map(Order::getNumber)
            .collect(Collectors.toList());
    }
}
