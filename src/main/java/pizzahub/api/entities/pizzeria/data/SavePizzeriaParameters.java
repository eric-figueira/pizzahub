package pizzahub.api.entities.pizzeria.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import pizzahub.api.presentation.validators.NullableNotEmpty.NullableNotEmpty;

import java.util.List;
import java.util.UUID;

public record SavePizzeriaParameters(
    @NotNull(message = "Code cannot be null")
    @Min(value = 100, message = "Code cannot be equal or lower than 100")
    @Max(value = 32700, message = "Code cannot be grater than 32700")
    Short code,

    @NotNull(message = "First contact cannot be null")
    @NotBlank(message = "First contact cannot be empty")
    @Size(min = 8, max = 20, message = "First contact must contain at least 8 and a max of 20 characters")
    @Pattern(regexp = "^\\(?(\\d{2})\\)?[-.\\s]?(\\d{4,5})[-.\\s]?(\\d{4})$")
    @JsonProperty("first_contact")
    String firstContact,

    @NotBlank(message = "Second contact cannot be empty")
    @Size(min = 8, max = 20, message = "Second contact must contain at least 8 and a max of 20 characters")
    @Pattern(regexp = "^\\(?(\\d{2})\\)?[-.\\s]?(\\d{4,5})[-.\\s]?(\\d{4})$")
    @JsonProperty("second_contact")
    String secondContact,

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email contact cannot be empty")
    @Size(min = 15, max = 50, message = "Email contact must contain at least 15 and a max of 50 characters")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    String email,

    @NotNull(message = "Address CEP cannot be null")
    @NotBlank(message = "Address CEP cannot be empty")
    @Size(min = 8, max = 8, message = "Address CEP must contain exactly 8 characters")
    @Pattern(regexp = "(^[0-9]{5})-?([0-9]{3}$)")
    String cep,

    @NotBlank(message = "Address complement cannot be empty")
    @Size(min = 3, max = 30, message = "Address complement must contain at least 3 and a max of 30 characters")
    String complement,

    @NotNull(message = "Address number cannot be null")
    @Min(value = 0, message = "Address number cannot be equal or lower than 0")
    @Max(value = 32000, message = "Address number cannot be grater than 32000")
    @JsonProperty("address_number")
    Short addressNumber,


    @NullableNotEmpty(message = "Workers IDs list cannot be empty if provided")

    @JsonProperty("workers_ids")
    List<UUID> workersIds
) {}
