package pizzahub.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pizzarias")
public class Pizzaria {
    @Id
    private short code;

    private String firstContact;
    private String secondContact;
    private String email;
    private String cep;
    private String telephoneNumber;

    public Pizzaria() {}
}
