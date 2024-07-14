package pizzahub.api.entities.pizzeria.data;

import java.util.List;

public record UpdatePizzeriaPartialParameters(
    Short code,
    String firstContact,
    String secondContact,
    String email,
    String cep,
    String complement,
    Short addressNumber,
    List<Long> workersIds
) {}
