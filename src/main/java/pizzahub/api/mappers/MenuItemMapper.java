package pizzahub.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pizzahub.api.entities.menuitem.MenuItem;
import pizzahub.api.entities.menuitem.data.MenuItemResponse;
import pizzahub.api.entities.menuitem.data.SaveMenuItemParameters;

import java.util.List;

@Mapper(componentModel = "spring", uses = { IngredientMapper.class })
public interface MenuItemMapper {
    MenuItemResponse fromEntityToResponse(MenuItem entity);
    List<MenuItemResponse> fromEntityListToResponseList(List<MenuItem> list);

    @Mapping(target = "ingredients", ignore = true)
    MenuItem fromSaveParametersToEntity(SaveMenuItemParameters parameters);

    @Mapping(target = "ingredients", ignore = true)
    void updateMenuItem(@MappingTarget MenuItem target, SaveMenuItemParameters parameters);
}
