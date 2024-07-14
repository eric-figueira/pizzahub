package pizzahub.api.entities.pizzeria.data;

import jakarta.validation.constraints.NotNull;
import pizzahub.api.entities.user.worker.Worker;

import java.util.List;

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

    @NotNull
    String complement,

    @NotNull
    Short addressNumber,

    List<Long> workersIds
) {}
