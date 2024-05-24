package pizzahub.api.entities.pizzeria.data;

public record FetchPizzeriaResponseDTO (
    Short code,
    String firstContact,
    String secondContact,
    String email,
    String cep,
    String streetName,
    String neighborhood,
    String city,
    String uf,
    String complement,
    Short addressNumber
) {}
