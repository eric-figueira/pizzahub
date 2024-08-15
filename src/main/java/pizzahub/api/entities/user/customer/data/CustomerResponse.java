package pizzahub.api.entities.user.customer.data;

import java.util.List;
import java.util.UUID;

public record CustomerResponse(
    UUID id,
    String fullName,
    String cpf,
    String email,
    String cep,
    String streetName,
    String neighborhood,
    String city,
    String uf,
    String complement,
    Short addressNumber
    // Integer numberOfOrders,
    // List<Short> ordersNumbers
) {}
