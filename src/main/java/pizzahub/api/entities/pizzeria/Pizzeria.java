package pizzahub.api.entities.pizzeria;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.pizzeria.data.CreatePizzeriaParameters;
import pizzahub.api.entities.user.worker.Worker;
import pizzahub.api.utils.RegexValidator;

import java.util.List;

@Getter
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
    private Short code;

    private String firstContact;
    private String secondContact;
    private String email;
    private String cep;
    private String streetName;
    private String neighborhood;
    private String city;
    private String uf;
    private String complement;
    private Short  addressNumber;

    @OneToMany(mappedBy = "pizzeria")
    private List<Worker> workers;

    public void setCode(Short code) {
        if (code <= 0) {
            throw new IllegalArgumentException("[Pizzeria]: Code cannot be equal or lower than zero");
        }
        this.code = code;
    }

    public void setFirstContact(String firstContact) {
        if (firstContact == null || firstContact.trim().isEmpty() || firstContact.length() < 10 || firstContact.length() > 50) {
            throw new IllegalArgumentException("Pizzeria's first contact must be between 10 and 50 characters and cannot be null or empty.");
        }
        this.firstContact = firstContact;
    }

    public void setSecondContact(String secondContact) {
        if (secondContact != null && (secondContact.trim().isEmpty() || secondContact.length() < 10 || secondContact.length() > 50)) {
            throw new IllegalArgumentException("Pizzeria's second contact must be between 10 and 50 characters and cannot be empty.");
        }
        this.secondContact = secondContact;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Pizzeria's email must be between 10 and 50 characters and cannot be empty");
        }
        if (!RegexValidator.validateEmail(email)) {
            throw new IllegalArgumentException("Pizzeria's email does not match pattern");
        }
        this.email = email;
    }

    public void setCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("Pizzeria's CEP cannot be null or empty");
        }
        if (!RegexValidator.validateCEP(cep)) {
            throw new IllegalArgumentException("Pizzeria's CEP does not match pattern");
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

    public void setWorkersList(List<Worker> workers) {
        if (workers == null || workers.isEmpty()) {
            throw new IllegalArgumentException("Pizzeria's workers list cannot be null or empty.");
        }
        this.workers = workers;
    }
}
