package pizzahub.api.entities.user.worker.data;

import pizzahub.api.entities.user.Role;

public record UpdateWorkerPartialParameters(
    String fullname,
    String email,
    String password,
    Role role
) {}
