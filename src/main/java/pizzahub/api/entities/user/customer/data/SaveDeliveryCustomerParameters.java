package pizzahub.api.entities.user.customer.data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SaveDeliveryCustomerParameters(
    @NotNull(message = "Fullname cannot be null")
    @NotBlank(message = "Fullname cannot be blank")
    @Size(min = 10, max = 50, message = "Fullname must contain at least 10 and a max of 50 characters")
    String fullname,

    @NotNull(message = "Cpf cannot be null")
    @NotBlank(message = "Cpf cannot be blank")
    @Pattern(regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$)")
    String cpf,

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    String email,

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 75, message = "Password must contain at least 8 and a max of 75 characters")
    String password,

    @NotNull(message = "Cep cannot be null")
    @NotBlank(message = "Cep cannot be blank")
    @Pattern(regexp = "(^[0-9]{5})-?([0-9]{3}$)")
    String cep,

    @NotNull(message = "Address complement cannot be null")
    @NotBlank(message = "Address complement cannot be blank")
    @Size(min = 3, max = 30, message = "Address complement must contain at least 3 and a max of 30 characters")
    String complement,

    @NotNull(message = "Addresss number cannot be null")
    @NotBlank(message = "Address number cannot be blank")
    @Min(value = 1, message = "Address number cannot be equal or lower than 0")
    @Max(value = 32000, message = "Address number cannot be grater than 32000")
    Short addressNumber
) {}
