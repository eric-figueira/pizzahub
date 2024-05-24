package pizzahub.api.entities.pizzeria.data;

import jakarta.validation.constraints.NotNull;

public record UpdatePizzeriaRequestDTO (
    @NotNull
    Short code,

    String firstContact,
    String secondContact,
    String email,
    String cep,
    String complement,
    Short addressNumber
) {}
