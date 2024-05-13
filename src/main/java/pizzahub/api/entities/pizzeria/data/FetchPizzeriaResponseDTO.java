package pizzahub.api.entities.pizzeria.data;

public record FetchPizzeriaResponseDTO (
    short code,
    String firstContact,
    String secondContact,
    String email,
    String cep,
    short addressNumber
)
{}
