package pizzahub.api.mappers;

import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.pizzeria.data.PizzeriaResponse;

public class PizzeriaMapper {
    public static PizzeriaResponse modelToResponse(Pizzeria model) {
        return new PizzeriaResponse(
            model.getId(),
            model.getCode(),
            model.getFirstContact(),
            model.getSecondContact(),
            model.getEmail(),
            model.getCep(),
            model.getStreetName(),
            model.getNeighborhood(),
            model.getCity(),
            model.getUf(),
            model.getComplement(),
            model.getAddressNumber(),
            model.getWorkers() != null ? model.getWorkers().size() : 0
        );
    }
}
