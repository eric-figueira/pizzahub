package pizzahub.api.entities.order;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pizzahub.api.entities.menuitem.MenuItem;
import pizzahub.api.entities.user.customer.Customer;

@Getter
@Setter
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
    @NotNull private Short number;

    @Temporal(TemporalType.DATE)
    @NotNull private Date orderDate;
    @NotNull private LocalTime orderTime;

    @NotNull private BigDecimal cost;
    private BigDecimal shippingTax;

    @Enumerated(EnumType.STRING)
    @NotNull private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @NotNull private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @NotNull private Customer customer;

    @ManyToMany
    @JoinTable(
        name = "order_menu_items",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItem> menuItems;
}
