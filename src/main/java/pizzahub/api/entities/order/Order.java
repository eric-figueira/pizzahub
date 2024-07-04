package pizzahub.api.entities.order;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
// import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pizzahub.api.entities.order.data.CreateOrderParameters;
import pizzahub.api.entities.order.data.OrderResponse;
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

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Temporal(TemporalType.DATE)
    private Date orderDate;
    private LocalTime orderTime;

    private BigDecimal cost;
    private BigDecimal shippingTax;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

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
}
