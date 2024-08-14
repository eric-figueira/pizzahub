package pizzahub.api.entities.pizzeria;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pizzahub.api.entities.user.worker.Worker;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "pizzeria")
public class Pizzeria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull private Short code;

    @NotNull private String firstContact;

    private String secondContact;

    @NotNull private String email;
    @NotNull private String cep;
    private String streetName;
    private String neighborhood;
    private String city;
    private String uf;

    private String complement;

    @NotNull private Short  addressNumber;

    @Temporal(TemporalType.DATE)
    @NotNull private Date createdAt;

    @OneToMany(mappedBy = "pizzeria")
    private List<Worker> workers;
}
