package pizzahub.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.pizzeria.data.PizzeriaResponse;
import pizzahub.api.entities.pizzeria.data.SavePizzeriaParameters;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PizzeriaMapper {
    PizzeriaResponse fromEntityToResponse(Pizzeria entity);

    @Mapping(target = "cep", ignore = true)
    @Mapping(target = "streetName", ignore = true)
    @Mapping(target = "neighborhood", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "uf", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "workers", ignore = true)
    Pizzeria fromSaveParametersToEntity(SavePizzeriaParameters parameters);

    List<PizzeriaResponse> fromEntityListToResponseList(List<Pizzeria> list);

    @Mapping(target = "cep", ignore = true)
    @Mapping(target = "streetName", ignore = true)
    @Mapping(target = "neighborhood", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "uf", ignore = true)
    @Mapping(target = "workers", ignore = true)
    void updatePizzeria(@MappingTarget Pizzeria target, SavePizzeriaParameters parameters);
}
