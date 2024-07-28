package pizzahub.api.entities.pizzeria.data;

import java.util.Date;
import java.util.List;

public record PizzeriaResponse(
    Short code,
    String firstContact,
    String secondContact,
    String email,
    String cep,
    String streetName,
    String neighborhood,
    String city,
    String uf,
    String complement,
    Short addressNumber,
    Date createdAt,
    List<Long> workersIds
) {}
