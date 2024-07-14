package pizzahub.api.entities.user.worker;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.user.Role;
import pizzahub.api.entities.user.worker.data.CreateWorkerRequestDTO;
import pizzahub.api.entities.user.worker.data.WorkerResponseDTO;
import pizzahub.api.utils.RegexValidator;

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

    @ManyToOne
    @JoinColumn(name = "pizzeria_code")
    private Pizzeria pizzeria;

    public void setFullname(String fullname) {
        if (fullname == null || fullname.trim().isEmpty() || fullname.length() < 5 || fullname.length() > 35) {
            throw new IllegalArgumentException("Worker's name must be between 5 and 50 characters and cannot be null or empty.");
        }
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Worker's email must be between 10 and 50 characters and cannot be empty");
        }
        if (!RegexValidator.validateEmail(email)) {
            throw new IllegalArgumentException("Worker's email does not match pattern");
        }
        this.email = email;
    }

    public void setPassword(String password) {
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

    public void setPizzeria(Pizzeria pizzeria) {
        if (pizzeria == null) {
            throw new IllegalArgumentException("Worker's cannot be null");
        }
        this.pizzeria = pizzeria;
    }

    public Worker(CreateWorkerRequestDTO data) throws Exception {
        setFullname(data.fullName());
        setEmail(data.email());
        setPassword(data.password());
        setRole(data.role());
    }

    public WorkerResponseDTO convertToResponseDTO() {
        return new WorkerResponseDTO(
            this.getId(),
            this.getFullname(),
            this.getEmail(),
            this.getPassword(),
            this.getRole(),
            this.getPizzeria().getCode());
    }
}
