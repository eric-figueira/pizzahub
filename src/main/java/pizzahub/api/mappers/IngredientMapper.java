package pizzahub.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.ingredient.data.IngredientResponse;
import pizzahub.api.entities.ingredient.data.SaveIngredientParameters;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    Ingredient fromSaveParametersToEntity(SaveIngredientParameters parameters);
    IngredientResponse fromEntityToResponse(Ingredient entity);

    @Mapping(target = "id", ignore = true)
    void updateIngredient(@MappingTarget Ingredient target, SaveIngredientParameters parameters);
}
