package pizzahub.api.entities.user.customer;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import pizzahub.api.entities.order.Order;
import pizzahub.api.entities.user.worker.Worker;
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
    private String CPF;

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

    public void setId(Long id) {
        if (id == 0) {
            throw new IllegalArgumentException("[Customer]: Id cannot be null or empty");
        }
        this.id = id;
    }

    public void setFullname(String fullname) {
        if (fullname == null || fullname.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: Fullname cannot be null or empty");
        }
        this.fullname = fullname;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: CPF cannot be null or empty");
        }
        if (!RegexValidator.validateCPF(cpf)) {
            throw new IllegalArgumentException("[Customer]: CPF does not match pattern");
        }
        this.CPF = cpf;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: Email cannot be null or empty");
        }
        if (!RegexValidator.validateEmail(email)) {
            throw new IllegalArgumentException("[Customer]: Email does not match pattern");
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: Password cannot be null or empty");
        }
        this.password = password;
    }

    public void setCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("[Customer]: CEP cannot be null or empty");
        }
        if (!RegexValidator.validateCEP(cep)) {
            throw new IllegalArgumentException("[Customer]: CEP does not match pattern");
        }
        this.cep = cep;
    }

    public void setStreetName(String streetName) {
        if (streetName == null || streetName.trim().isEmpty() || streetName.length() < 10 || streetName.length() > 50) {
            throw new IllegalArgumentException("Pizzeria's street name must be between 10 and 50 characters and cannot be empty");
        }
        this.streetName = streetName;
    }

    public void setNeighborhood(String neighborhood) {
        if (neighborhood == null || neighborhood.trim().isEmpty() || neighborhood.length() < 5 || neighborhood.length() > 50) {
            throw new IllegalArgumentException("Pizzeria's neighborhood must be between 5 and 50 characters and cannot be empty");
        }
        this.neighborhood = neighborhood;
    }

    public void setCity(String city) {
        if (city == null || city.trim().isEmpty() || city.length() < 5 || city.length() > 50) {
            throw new IllegalArgumentException("Pizzeria's city must be between 5 and 50 characters and cannot be empty");
        }
        this.city = city;
    }

    public void setUf(String uf) {
        if (uf == null || uf.trim().isEmpty() || uf.length() != 2) {
            throw new IllegalArgumentException("Pizzeria's UF must be exactly 2 characters long and cannot be empty");
        }
        this.uf = uf;
    }

    public void setComplement(String complement) {
        if (complement == null || complement.trim().isEmpty() || complement.length() < 10 || complement.length() > 50) {
            throw new IllegalArgumentException("Pizzeria's complement must be between 10 and 50 characters and cannot be empty");
        }
        this.complement = complement;
    }

    public void setAddressNumber(Short addressNumber) {
        if (addressNumber == null || addressNumber <= 0) {
            throw new IllegalArgumentException("Pizzeria's address number must be greater than zero and less than or equal to 250");
        }
        this.addressNumber = addressNumber;
    }
}
