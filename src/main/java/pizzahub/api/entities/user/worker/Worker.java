package pizzahub.api.entities.user.worker;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.user.Role;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "worker")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull private UUID id;

    @NotNull private String fullName;
    @NotNull private String email;
    @NotNull private String password;

    @Temporal(TemporalType.DATE)
    @NotNull private Date createdAt;

    @Enumerated(EnumType.STRING)
    @NotNull private Role role;

    @ManyToOne
    @JoinColumn(name = "pizzeria_code")
    private Pizzeria pizzeria;
}
