package pizzahub.api.entities.pizzeria.data;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdatePizzeriaParameters(
    @NotNull
    Short code,

    @NotNull
    String firstContact,
    String secondContact,

    @NotNull
    String email,

    @NotNull
    String cep,

    @NotNull
    String complement,

    @NotNull
    Short addressNumber,

    @NotNull
    List<Long> workersIds
) {}
