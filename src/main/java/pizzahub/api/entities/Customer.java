package pizzahub.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;
    private String cpf;
    private String email;
    private String password;
    private String cep;
    private String houseNumber;
    private String houseComplement;

    public Customer() {}

    // public Customer(Long id, String fullname, String email, String password, String cep, String houseNumber, String houseComplement)
    // {
    //     this.setId(id);
    //     this.setFullname(fullname);
    //     this.setEmail(email);
    //     this.setPassword(password);
    //     this.setCep(cep);
    //     this.setHouseNumber(houseNumber);
    //     this.setHouseComplement(houseComplement);
    // }

    // public Customer(Long id, String fullname, String cpf)
    // {
    //     this.setId(id);
    //     this.setFullname(fullname);
    //     this.setCep(cep);
    // }

    public Long getId() { return id; }
    public String getFullname() { return fullname; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getCep() { return cep; }
    public String getHouseNumber() { return houseNumber; }
    public String getHouseComplement() { return houseComplement; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
    public void setHouseComplement(String houseComplement) {
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
