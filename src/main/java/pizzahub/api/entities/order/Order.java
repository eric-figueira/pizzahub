package pizzahub.api.entities.order;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private short number;

    @OneToOne
    @JoinColumn(name = "id")
    private UUID clientId;

    private Date orderDate;
    private LocalTime orderTime;

    private BigDecimal shippingTax;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public void setId(Long id) {
        if (id <= 0L) {
            throw new IllegalArgumentException("[Order]: Order id cannot be equal or lower than zero");
        }
        this.id = id;
    }

    public void setNumber(short number) {
        if (number <= 0) {
            throw new IllegalArgumentException("[Order]: Number cannot be equal or lower than zero");
        }
        this.number = number;
    }

    public void setClientId(UUID clientId) {
        if (clientId == null || clientId.toString().isEmpty()) {
            throw new IllegalArgumentException("[Order]: Client id cannot be null");
        }
        this.clientId = clientId;
    }

    public void setOrderDate(Date orderDate) {
        if (orderDate == null || orderDate.after(new Date())) {
            throw new IllegalArgumentException("[Order]: Date cannot be null or in the future");
        }
        this.orderDate = orderDate;
    }

    public void setOrderTime(LocalTime orderTime) {
        if (orderTime == null) {
            throw new IllegalArgumentException("[Order]: Time cannot be null");
        }
        this.orderTime = orderTime;
    }

    public void setShippingTax(BigDecimal shippingTax) {
        if (shippingTax == null || shippingTax.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[Order]: Shipping tax must be greater than or equal to zero");
        }
        this.shippingTax = shippingTax;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("[Order]: Payment method cannot be null");
        }
        this.paymentMethod = paymentMethod;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        if (orderStatus == null) {
            throw new IllegalArgumentException("[Order]: Status cannot be null");
        }
        this.orderStatus = orderStatus;
    }

    // public Order() {}
}
