package pizzahub.api.entities.user.worker.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pizzahub.api.entities.user.Role;

public record UpdateWorkerPartialParameters(
    @NotBlank(message = "Full name cannot be empty")
    @Size(min = 10, max = 50, message = "Full name must contain at least 15 and a max of 50 characters")
    @JsonProperty("fullname")
    String fullName,

    @NotBlank(message = "Email contact cannot be empty")
    @Size(min = 15, max = 50, message = "Email contact must contain at least 15 and a max of 50 characters")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    String email,

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 75, message = "Password must contain at least 8 and a max of 75 characters")
    String password,

    Role role,
    Short pizzeriaCode
) {}
