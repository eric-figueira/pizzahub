package pizzahub.api.entities.user.worker.data;

import jakarta.validation.constraints.NotNull;
import pizzahub.api.entities.user.Role;

public record CreateWorkerParameters(
    @NotNull
    String fullname,

    @NotNull
    String email,

    @NotNull
    String password,

    @NotNull
    Role role
) {}
