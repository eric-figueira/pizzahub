package pizzahub.api.infrastructure.cep;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String cep;
    private String streetName;
    private String complement;
    private String neighborhood;
    private String locale;
    private String uf;
}
