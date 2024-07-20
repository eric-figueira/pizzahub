package pizzahub.api.entities.order;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
// import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.menuitem.MenuItem;
import pizzahub.api.entities.order.data.CreateOrderParameters;
import pizzahub.api.entities.order.data.OrderResponse;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.user.customer.Customer;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Short number;

    @Temporal(TemporalType.DATE)
    private Date orderDate;
    private LocalTime orderTime;

    private BigDecimal cost;
    private BigDecimal shippingTax;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(
        name = "order_menu_items",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    @NotEmpty
    @NotNull
    private List<MenuItem> menuItems;

    public void setNumber(Short number) {
        if (number == null || number <= 0) {
            throw new IllegalArgumentException("Order's number must be a positive number and cannot be null");
        }
        this.number = number;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Order's customer cannot be null");
        }
        this.customer = customer;
    }

    public void setOrderDate(Date orderDate) {
        if (orderDate == null || orderDate.after(new Date())) {
            throw new IllegalArgumentException("Order's date cannot be null or in the future");
        }
        this.orderDate = orderDate;
    }

    public void setOrderTime(LocalTime orderTime) {
        if (orderTime == null) {
            throw new IllegalArgumentException("Order's time cannot be null");
        }
        this.orderTime = orderTime;
    }

    public void setCost(BigDecimal cost) {
        if (cost == null || cost.signum() <= 0) {
            throw new IllegalArgumentException("Order's cost tax must be greater than or equal to zero");
        }
        this.cost = cost;
    }

    public void setShippingTax(BigDecimal shippingTax) {
        if (shippingTax == null || shippingTax.signum() <= 0) {
            throw new IllegalArgumentException("Order's shipping tax must be greater than or equal to zero");
        }
        this.shippingTax = shippingTax;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Order's payment method cannot be null");
        }
        this.paymentMethod = paymentMethod;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        if (orderStatus == null) {
            throw new IllegalArgumentException("Order's status cannot be null");
        }
        this.orderStatus = orderStatus;
    }

    public void setMenuItems(List<MenuItem> menuitems) {
        if (menuitems == null || menuitems.isEmpty()) {
            throw new IllegalArgumentException("Order's menu items list cannot be null or empty.");
        }
        this.menuItems = menuitems;
    }
}
