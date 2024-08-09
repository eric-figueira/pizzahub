package pizzahub.api.entities.user.customer;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import pizzahub.api.entities.order.Order;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fullname;

    // For presential orders only. Null when the order is by delivery
    private String cpf;

    // For delivery orders only. Null when the order is presential
    private String email;
    private String password;
    private String cep;
    private String streetName;
    private String neighborhood;
    private String city;
    private String uf;
    private String complement;
    private Short  addressNumber;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}
