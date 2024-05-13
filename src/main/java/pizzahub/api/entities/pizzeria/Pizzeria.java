package pizzahub.api.entities.pizzeria;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pizzahub.api.entities.pizzeria.data.CreatePizzeriaRequestDTO;
import pizzahub.api.entities.pizzeria.data.FetchPizzeriaResponseDTO;
import pizzahub.api.utils.RegexValidator;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "pizzeria")
public class Pizzeria {
    @Id
    private short code;

    private String firstContact;
    private String secondContact;
    private String email;
    private String cep;
    private Short addressNumber;

    public void setCode(short code) {
        if (code <= 0) {
            throw new IllegalArgumentException("[Pizzeria]: Code cannot be equal or lower than zero");
        }
        this.code = code;
    }

    public void setFirstContact(String firstContact) {
        if (firstContact == null || firstContact.trim().isEmpty()) {
            throw new IllegalArgumentException("[Pizzeria]: First contact cannot be null or empty");
        }
        this.firstContact = firstContact;
    }

    public void setSecondContact(String secondContact) {
        if (secondContact == null || secondContact.trim().isEmpty()) {
            throw new IllegalArgumentException("[Pizzeria]: Second contact cannot be null or empty");
        }
        this.secondContact = secondContact;
    }

    public void setEmail(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("[Pizzeria]: Email cannot be null or empty");
        }
        if (!RegexValidator.validateEmail(email)) {
            throw new IllegalArgumentException("[Pizzeria]: Email does not match pattern");
        }
        this.email = email;
    }

    public void setCep(String cep) throws Exception {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("[Pizzeria]: CEP cannot be null or empty");
        }
        if (!RegexValidator.validateCEP(cep)) {
            throw new IllegalArgumentException("[Pizzeria]: CEP does not match pattern");
        }
        this.cep = cep;
    }

    public void setAddressNumber(Short addressNumber) {
        if (addressNumber == null || addressNumber == 0) {
            throw new IllegalArgumentException("[Pizzeria]: Telephone cannot be null or empty");
        }
        this.addressNumber = addressNumber;
    }

    public Pizzeria(CreatePizzeriaRequestDTO pizzeria) throws Exception {
        setAddressNumber(pizzeria.addressNumber());
        setCep(pizzeria.cep());
        setCode(pizzeria.code());
        setEmail(pizzeria.email());
        setFirstContact(pizzeria.firstContact());
        if (pizzeria.secondContact() != null)
            setSecondContact(pizzeria.secondContact());
    }

    public FetchPizzeriaResponseDTO convertToResponseDTO() {
        return new FetchPizzeriaResponseDTO(
            this.getCode(),
            this.getFirstContact(),
            this.getSecondContact(),
            this.getEmail(),
            this.getCep(),
            this.getAddressNumber()
        );
    }
}
