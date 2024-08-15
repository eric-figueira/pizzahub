package pizzahub.api.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pizzahub.api.entities.user.customer.Customer;
import pizzahub.api.entities.user.customer.data.SaveCustomerParameters;
import pizzahub.api.entities.user.customer.data.SaveDeliveryCustomerParameters;
import pizzahub.api.entities.user.customer.data.CustomerResponse;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "orders", ignore = true)
    Customer fromCreateParametersToEntity(SaveCustomerParameters parameters);

    Customer fromCreateDeliveryParametersToEntity(SaveDeliveryCustomerParameters parameters);

    //@Mapping(target = "numberOfOrders", ignore = true)
    //@Mapping(target = "ordersNumbers", ignore = true)
    CustomerResponse fromEntityToResponse(Customer entity);

    List<CustomerResponse> fromEntityListToResponseList(List<Customer> list);

    @Mapping(target = "orders", ignore = true)
    void updateCustomer(@MappingTarget Customer target, SaveCustomerParameters parameters);

    @Mapping(target = "orders", ignore = true)
    void updateCustomer(@MappingTarget Customer target, SaveDeliveryCustomerParameters parameters);
}
