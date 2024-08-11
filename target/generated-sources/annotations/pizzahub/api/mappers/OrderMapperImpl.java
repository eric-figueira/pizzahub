package pizzahub.api.mappers;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pizzahub.api.entities.menuitem.data.MenuItemResponse;
import pizzahub.api.entities.order.Order;
import pizzahub.api.entities.order.OrderStatus;
import pizzahub.api.entities.order.PaymentMethod;
import pizzahub.api.entities.order.data.CreateOrderParameters;
import pizzahub.api.entities.order.data.OrderResponse;
import pizzahub.api.entities.order.data.UpdateOrderParameters;
import pizzahub.api.entities.user.customer.data.CustomerResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-11T16:16:13-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (BellSoft)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private MenuItemMapper menuItemMapper;

    @Override
    public OrderResponse fromEntityToResponse(Order entity) {
        if ( entity == null ) {
            return null;
        }

        Short number = null;
        Date orderDate = null;
        LocalTime orderTime = null;
        BigDecimal cost = null;
        BigDecimal shippingTax = null;
        PaymentMethod paymentMethod = null;
        OrderStatus orderStatus = null;
        List<MenuItemResponse> menuItems = null;
        CustomerResponse customer = null;

        number = entity.getNumber();
        orderDate = entity.getOrderDate();
        orderTime = entity.getOrderTime();
        cost = entity.getCost();
        shippingTax = entity.getShippingTax();
        paymentMethod = entity.getPaymentMethod();
        orderStatus = entity.getOrderStatus();
        menuItems = menuItemMapper.fromEntityListToResponseList( entity.getMenuItems() );
        customer = customerMapper.fromEntityToResponse( entity.getCustomer() );

        OrderResponse orderResponse = new OrderResponse( number, orderDate, orderTime, cost, shippingTax, paymentMethod, orderStatus, menuItems, customer );

        return orderResponse;
    }

    @Override
    public List<OrderResponse> fromEntityListToResponseList(List<Order> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderResponse> list1 = new ArrayList<OrderResponse>( list.size() );
        for ( Order order : list ) {
            list1.add( fromEntityToResponse( order ) );
        }

        return list1;
    }

    @Override
    public Order fromCreateParametersToEntity(CreateOrderParameters parameters) {
        if ( parameters == null ) {
            return null;
        }

        Order order = new Order();

        order.setNumber( parameters.number() );
        order.setCost( parameters.cost() );
        order.setShippingTax( parameters.shippingTax() );

        return order;
    }

    @Override
    public Order fromUpdateParametersToEntity(UpdateOrderParameters parameters) {
        if ( parameters == null ) {
            return null;
        }

        Order order = new Order();

        order.setNumber( parameters.number() );
        order.setCost( parameters.cost() );
        order.setShippingTax( parameters.shippingTax() );
        order.setPaymentMethod( parameters.paymentMethod() );
        order.setOrderStatus( parameters.orderStatus() );

        return order;
    }

    @Override
    public void updateOrder(Order target, UpdateOrderParameters parameters) {
        if ( parameters == null ) {
            return;
        }

        target.setNumber( parameters.number() );
        target.setCost( parameters.cost() );
        target.setShippingTax( parameters.shippingTax() );
        target.setPaymentMethod( parameters.paymentMethod() );
        target.setOrderStatus( parameters.orderStatus() );
    }
}
