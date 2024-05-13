package pizzahub.api.entities.pizzeria.data;

import jakarta.validation.constraints.NotNull;

public record UpdatePizzeriaRequestDTO (
    @NotNull
    String firstContact,

    String secondContact,

    @NotNull
    String email,

    @NotNull
    String cep,

    @NotNull
    short addressNumber
)
{}
