package pizzahub.api.entities;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private short number;

    private Date orderDate;
    private LocalTime orderTime;

    private BigDecimal shippingTax;

    public Order() {}
}
