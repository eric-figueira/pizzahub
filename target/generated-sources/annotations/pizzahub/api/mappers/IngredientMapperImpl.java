package pizzahub.api.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.ingredient.data.IngredientResponse;
import pizzahub.api.entities.ingredient.data.SaveIngredientParameters;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-09T12:33:13-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.39.0.v20240725-1906, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class IngredientMapperImpl implements IngredientMapper {

    @Override
    public Ingredient fromSaveParametersToEntity(SaveIngredientParameters parameters) {
        if ( parameters == null ) {
            return null;
        }

        Ingredient ingredient = new Ingredient();

        ingredient.setName( parameters.name() );
        ingredient.setSlug( parameters.slug() );

        return ingredient;
    }

    @Override
    public IngredientResponse fromEntityToResponse(Ingredient entity) {
        if ( entity == null ) {
            return null;
        }

        String slug = null;
        String name = null;

        slug = entity.getSlug();
        name = entity.getName();

        IngredientResponse ingredientResponse = new IngredientResponse( slug, name );

        return ingredientResponse;
    }

    @Override
    public List<IngredientResponse> fromEntityListToResponseList(List<Ingredient> list) {
        if ( list == null ) {
            return null;
        }

        List<IngredientResponse> list1 = new ArrayList<IngredientResponse>( list.size() );
        for ( Ingredient ingredient : list ) {
            list1.add( fromEntityToResponse( ingredient ) );
        }

        return list1;
    }

    @Override
    public void updateIngredient(Ingredient target, SaveIngredientParameters parameters) {
        if ( parameters == null ) {
            return;
        }

        target.setName( parameters.name() );
        target.setSlug( parameters.slug() );
    }
}
