package pizzahub.api.entities.user.worker.data;

import jakarta.validation.constraints.NotNull;
import pizzahub.api.entities.user.Role;
import pizzahub.api.entities.user.worker.Worker;

public record CreateWorkerRequestDTO(
    @NotNull
    String fullName,

    @NotNull
    String email,

    @NotNull
    String password,

    @NotNull
    Role role,

    Short pizzeria_id
) {}
