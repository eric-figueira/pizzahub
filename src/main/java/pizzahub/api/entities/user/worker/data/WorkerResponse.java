package pizzahub.api.entities.user.worker.data;

import pizzahub.api.entities.user.Role;

import java.util.Date;
import java.util.UUID;

public record WorkerResponse(
    UUID id,
    String fullName,
    String email,
    Role role,
    Date createdAt,
    Short pizzeriaCode
) {}
