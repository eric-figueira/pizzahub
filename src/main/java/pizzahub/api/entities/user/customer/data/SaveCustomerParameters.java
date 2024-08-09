package pizzahub.api.entities.user.customer.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SaveCustomerParameters(
    @NotNull(message = "Fullname cannot be null")
    @NotBlank(message = "Fullname cannot be blank")
    @Size(min = 10, max = 50, message = "Fullname must contain at least 10 and a max of 50 characters")
    String fullname,

    @NotNull(message = "Cpf cannot be null")
    @NotBlank(message = "Cpf cannot be blank")
    @Pattern(regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$)")
    String cpf
) {}
