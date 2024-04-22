package pizzahub.api.entities.user.worker;

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
import pizzahub.api.entities.pizzaria.Pizzaria;
import pizzahub.api.entities.user.Role;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "worker")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "code")
    private Pizzaria pizzaria;

    public void setId(Long id) throws Exception {
        if (id.toString().isEmpty()) {
            throw new IllegalArgumentException("[Worker]: Id cannot be null or empty");
        }
        this.id = id;
    }

    public void setFullname(String fullname) throws Exception {
        if (fullname == null || fullname.trim().isEmpty()) {
            throw new IllegalArgumentException("[Worker]: Fullname cannot be null or empty");
        }
        this.fullname = fullname;
    }

    public void setEmail(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("[Worker]: Email cannot be null or empty");
        }
        this.email = email;
    }

    public void setPassword(String password) throws Exception {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("[Worker]: Password cannot be null or empty");
        }
        this.password = password;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("[Worker]: Role cannot be null");
        }
        this.role = role;
    }

    public void setPizzaria(Pizzaria pizzaria) {
        if (pizzaria == null) {
            throw new IllegalArgumentException("[Worker]: Pizzaria cannot be null");
        }
        this.pizzaria = pizzaria;
    }

    // public Worker() {}
}
