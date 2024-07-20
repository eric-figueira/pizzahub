package pizzahub.api.entities.user.customer.data;

import jakarta.validation.constraints.NotNull;

public record CreateCustomerParameters(
    @NotNull
    String fullname,

    @NotNull
    String cpf
) {}
