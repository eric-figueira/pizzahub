package pizzahub.api.mappers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pizzahub.api.entities.ingredient.data.IngredientResponse;
import pizzahub.api.entities.menuitem.MenuItem;
import pizzahub.api.entities.menuitem.data.MenuItemResponse;
import pizzahub.api.entities.menuitem.data.SaveMenuItemParameters;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-15T08:17:04-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Azul Systems, Inc.)"
)
@Component
public class MenuItemMapperImpl implements MenuItemMapper {

    @Autowired
    private IngredientMapper ingredientMapper;

    @Override
    public MenuItemResponse fromEntityToResponse(MenuItem entity) {
        if ( entity == null ) {
            return null;
        }

        String slug = null;
        String name = null;
        BigDecimal price = null;
        List<IngredientResponse> ingredients = null;

        slug = entity.getSlug();
        name = entity.getName();
        price = entity.getPrice();
        ingredients = ingredientMapper.fromEntityListToResponseList( entity.getIngredients() );

        MenuItemResponse menuItemResponse = new MenuItemResponse( slug, name, price, ingredients );

        return menuItemResponse;
    }

    @Override
    public List<MenuItemResponse> fromEntityListToResponseList(List<MenuItem> list) {
        if ( list == null ) {
            return null;
        }

        List<MenuItemResponse> list1 = new ArrayList<MenuItemResponse>( list.size() );
        for ( MenuItem menuItem : list ) {
            list1.add( fromEntityToResponse( menuItem ) );
        }

        return list1;
    }

    @Override
    public MenuItem fromSaveParametersToEntity(SaveMenuItemParameters parameters) {
        if ( parameters == null ) {
            return null;
        }

        MenuItem menuItem = new MenuItem();

        menuItem.setPrice( parameters.price() );
        menuItem.setSlug( parameters.slug() );
        menuItem.setName( parameters.name() );

        return menuItem;
    }

    @Override
    public void updateMenuItem(MenuItem target, SaveMenuItemParameters parameters) {
        if ( parameters == null ) {
            return;
        }

        target.setPrice( parameters.price() );
        target.setSlug( parameters.slug() );
        target.setName( parameters.name() );
    }
}
