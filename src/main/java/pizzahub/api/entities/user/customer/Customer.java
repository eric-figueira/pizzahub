package pizzahub.api.entities.user.customer;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import pizzahub.api.utils.RegexValidator;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;

    // For presential orders only. Null when the order is by delivery
    private String cpf;

    // For delivery orders only. Null when the order is presential
    private String email;
    private String password;
    private String cep;
    private String houseNumber;
    private String houseComplement;

    public void setId(Long id) throws Exception {
        if (id == 0) {
            throw new IllegalArgumentException("[Customer]: Id cannot be null or empty");
        }
        this.id = id;
    }

    public void setFullname(String fullname) throws Exception {
        if (fullname == null || fullname.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: Fullname cannot be null or empty");
        }
        this.fullname = fullname;
    }

    public void setCpf(String cpf) throws Exception {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: CPF cannot be null or empty");
        }
        if (!RegexValidator.validateCPF(cpf)) {
            throw new IllegalArgumentException("[Customer]: CPF does not match pattern");
        }
        this.cpf = cpf;
    }

    public void setEmail(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: Email cannot be null or empty");
        }
        if (!RegexValidator.validateEmail(email)) {
            throw new IllegalArgumentException("[Customer]: Email does not match pattern");
        }
        this.email = email;
    }

    public void setPassword(String password) throws Exception {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: Password cannot be null or empty");
        }
        this.password = password;
    }

    public void setCep(String cep) throws Exception {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: CEP cannot be null or empty");
        }
        if (!RegexValidator.validateCEP(cep)) {
            throw new IllegalArgumentException("[Customer]: CEP does not match pattern");
        }
        this.cep = cep;
    }

    public void setHouseNumber(String houseNumber) throws Exception {
        if (houseNumber == null || houseNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: House number cannot be null or empty");
        }
        this.houseNumber = houseNumber;
    }

    public void setHouseComplement(String houseComplement) throws Exception {
        if (houseComplement == null || houseComplement.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: House complement cannot be null or empty");
        }

        this.houseComplement = houseComplement;
    }
}
