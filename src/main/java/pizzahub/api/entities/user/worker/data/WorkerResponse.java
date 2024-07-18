package pizzahub.api.entities.user.worker.data;

import jakarta.validation.constraints.NotNull;
import pizzahub.api.entities.user.Role;

public record WorkerResponse(
    @NotNull
    Long id,

    @NotNull
    String fullName,

    @NotNull
    String email,

    @NotNull
    Role role,

    Short pizzeriaCode
) {}
