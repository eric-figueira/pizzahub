package pizzahub.api.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pizzahub.api.utils.RegexValidator;

@Entity
@Table(name = "customers")
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
    private String houseNumber;
    private String houseComplement;

    public Customer() {}

    public UUID getId() { return this.id; }
    public String getFullname() { return this.fullname; }
    public String getCpf() { return this.cpf; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public String getCep() { return this.cep; }
    public String getHouseNumber() { return this.houseNumber; }
    public String getHouseComplement() { return this.houseComplement; }

    public void setId(UUID id) throws Exception {
        if (id.toString().isEmpty()) {
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

    @Override
    public int hashCode ()
    {
        int ret = 23,
            k = 11;

        ret += k * ret + this.id.hashCode();
        ret += k * ret + String.valueOf(this.fullname).hashCode();
        ret += k * ret + String.valueOf(this.cpf).hashCode();
        ret += k * ret + String.valueOf(this.email).hashCode();
        ret += k * ret + String.valueOf(this.password).hashCode();
        ret += k * ret + String.valueOf(this.cep).hashCode();
        ret += k * ret + String.valueOf(this.houseNumber).hashCode();
        ret += k * ret + String.valueOf(this.houseComplement).hashCode();

        if (ret < 0)
            ret -= ret;

        return ret;
    }

    public boolean equals (Customer other)
    {
        if (this == other) return true;
        if (other == null) return false;

        if (this.id != other.id) return false;
        if (!this.fullname.equals(other.fullname)) return false;
        if (!this.cpf.equals(other.cpf)) return false;
        if (!this.email.equals(other.email)) return false;
        if (!this.password.equals(other.password)) return false;
        if (!this.cep.equals(other.cep)) return false;
        if (!this.houseNumber.equals(other.houseNumber)) return false;
        if (!this.houseComplement.equals(other.houseComplement)) return false;

        return true;
    }

    @Override
    public String toString ()
    {
        return "Id: " + this.id.toString() + " - " +
            "Fullname: " + this.fullname + " - " +
            "CPF: " + this.cpf + " - " +
            "Email: " + this.email + " - " +
            "Password: " + this.password + " - " +
            "CEP: " + this.cep + " - " +
            "House Number: " + this.houseNumber + " - " +
            "House Complement: " + this.houseComplement + " - ";
    }
}
