package pizzahub.api.entities.pizzeria.data;

import jakarta.validation.constraints.NotNull;

public record CreatePizzeriaParameters(
    @NotNull
    Short code,

    @NotNull
    String firstContact,

    String secondContact,

    @NotNull
    String email,

    @NotNull
    String cep,

    String complement,

    @NotNull
    Short addressNumber
) {}
