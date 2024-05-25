package pizzahub.api.entities.user.worker.data;

import jakarta.validation.constraints.NotNull;
import pizzahub.api.entities.user.Role;

public record WorkerResponseDTO(
    @NotNull
    Long id,

    @NotNull
    String fullName,

    @NotNull
    String email,

    @NotNull
    String password,

    @NotNull
    Role role,

    Short pizzeria_id) {
}
