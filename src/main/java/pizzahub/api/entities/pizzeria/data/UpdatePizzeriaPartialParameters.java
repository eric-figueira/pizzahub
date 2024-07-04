package pizzahub.api.entities.pizzeria.data;

public record UpdatePizzeriaPartialParameters(
    Short code,
    String firstContact,
    String secondContact,
    String email,
    String cep,
    String complement,
    Short addressNumber
) {}
