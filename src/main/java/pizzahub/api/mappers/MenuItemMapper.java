package pizzahub.api.mappers;

import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.menuitem.MenuItem;
import pizzahub.api.entities.menuitem.data.MenuItemResponse;

import java.util.stream.Collectors;

public class MenuItemMapper {
    public static MenuItemResponse modelToResponse(MenuItem model) {
        return new MenuItemResponse(
            model.getId(),
            model.getPrice(),
            model.getName(),
            model.getIngredients()
                .stream()
                .collect(Collectors.toMap(Ingredient::getId, Ingredient::getName))
        );
    }
}
