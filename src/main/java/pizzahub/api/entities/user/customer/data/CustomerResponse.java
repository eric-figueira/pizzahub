package pizzahub.api.entities.user.customer.data;

import java.util.List;

public record CustomerResponse(
    String fullname,
    String cpf,
    String email,
    String cep,
    String streetName,
    String neighborhood,
    String city,
    String uf,
    String complement,
    Short addressNumber,
    Integer numberOfOrders,
    List<Short> ordersNumbers
) {}
