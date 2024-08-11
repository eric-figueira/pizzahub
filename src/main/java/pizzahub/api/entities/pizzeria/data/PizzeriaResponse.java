package pizzahub.api.entities.pizzeria.data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    List<UUID> workersIds
) {}
