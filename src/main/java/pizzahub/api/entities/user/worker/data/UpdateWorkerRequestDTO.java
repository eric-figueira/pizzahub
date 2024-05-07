package pizzahub.api.entities.user.worker.data;

import jakarta.validation.constraints.NotNull;
import pizzahub.api.entities.user.Role;

public record UpdateWorkerRequestDTO (
    @NotNull
    Long id,

    String fullname,
    String email,
    String password,
    Role role,
    Short pizzeriaCode
) {}
