package pizzahub.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.ingredient.data.IngredientResponse;
import pizzahub.api.entities.ingredient.data.SaveIngredientParameters;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    Ingredient fromSaveParametersToEntity(SaveIngredientParameters parameters);
    IngredientResponse fromEntityToResponse(Ingredient entity);
    List<IngredientResponse> fromEntityListToResponseList(List<Ingredient> list);

    @Mapping(target = "id", ignore = true)
    void updateIngredient(@MappingTarget Ingredient target, SaveIngredientParameters parameters);
}
