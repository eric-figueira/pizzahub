package pizzahub.api.entities.user.customer.data;

import jakarta.validation.constraints.NotNull;

public record CreateDeliveryCustomerParameters(
    @NotNull
    String fullname,

    @NotNull
    String cpf,

    @NotNull
    String email,

    @NotNull
    String password,

    @NotNull
    String cep,

    @NotNull
    String complement,

    @NotNull
    Short addressNumber
) {}
