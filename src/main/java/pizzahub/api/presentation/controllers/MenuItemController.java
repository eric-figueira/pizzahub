package pizzahub.api.presentation.controllers;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.menuitem.MenuItem;
import pizzahub.api.entities.menuitem.data.CreateMenuItemParameters;
import pizzahub.api.entities.menuitem.data.MenuItemResponse;
import pizzahub.api.entities.menuitem.data.UpdateMenuItemParameters;
import pizzahub.api.entities.menuitem.data.UpdateMenuItemPartialParameters;

import pizzahub.api.mappers.MenuItemMapper;
import pizzahub.api.repositories.IngredientRepository;
import pizzahub.api.repositories.MenuItemRepository;

import pizzahub.api.presentation.Response;

@RestController
@RequestMapping(value = "/menuitems")
public class MenuItemController {

    @Autowired
    private MenuItemRepository repository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public ResponseEntity<Response> fetchAll(
        @RequestParam(value = "page", defaultValue = "1") short page,
        @RequestParam(value = "perPage", defaultValue = "30") short perPage,
        @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
        @RequestParam(value = "order", defaultValue = "asc") String order
    ) {
        List<MenuItem> all = this.repository.findAll();

        // pagination
        short start = (short) ((page - 1) * perPage);

        double numberOfGroups = (double) all.size() / perPage;
        short lastGroupNumber = (short) Math.ceil(numberOfGroups);

        short end = page == lastGroupNumber ? (short) all.size() : (short) (page * perPage);

        if (start >= all.size()) {
            throw new IllegalArgumentException(
                "The pagination parameters are invalid. Please ensure that 'page' is smaller than the total data size"
            );
        }

        List<MenuItem> paginated = all.subList(start, end);

        // ordination
        if ("price".equals(orderBy)) {
            if (order.equalsIgnoreCase("asc"))
                paginated.sort(Comparator.comparing(MenuItem::getPrice));
            else
                paginated.sort(Comparator.comparing(MenuItem::getPrice).reversed());
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched all menu items",
                paginated.stream()
                    .map(MenuItemMapper::modelToResponse)
                    .collect(Collectors.toList())
            ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchById(@PathVariable("id") Long menuItemId) {
        MenuItem menuItem = this.repository.findById(menuItemId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch menu item with specified ID"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched menu item with specified id",
                MenuItemMapper.modelToResponse(menuItem)
            ));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid CreateMenuItemParameters body) {
        MenuItem newMenuItem = new MenuItem();
        newMenuItem.setName(body.name());
        newMenuItem.setPrice(body.price());

        List<Ingredient> ingredients = body.ingredientsIds()
            .stream()
            .map(ingredientId -> this.ingredientRepository
                .findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve one of the ingredients informed by ID")))
            .collect(Collectors.toList());

        newMenuItem.setIngredients(ingredients);

        MenuItem created = this.repository.save(newMenuItem);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully created new menu item",
                MenuItemMapper.modelToResponse(created)
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long menuItemId) {
        MenuItem exists = this.repository.findById(menuItemId)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified id in order to remove it"));

        this.repository.deleteById(menuItemId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("Successfully deleted menu item with specified id", null));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updatePartial(
        @PathVariable("id") Long id,
        @RequestBody @Valid UpdateMenuItemPartialParameters body
    ) {
        MenuItem current = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified id in order to remove it"));

        if (body.name()  != null) current.setName(body.name());
        if (body.price() != null) current.setPrice(body.price());

        if (body.ingredientsIds() != null) {
            List<Ingredient> ingredients = body.ingredientsIds()
                .stream()
                .map(ingredientId -> this.ingredientRepository
                    .findById(ingredientId)
                    .orElseThrow(EntityNotFoundException::new))
                .collect(Collectors.toList());

            current.setIngredients(ingredients);
        }

        MenuItem updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated Menu Item",
                MenuItemMapper.modelToResponse(updated)
            ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(
        @PathVariable("id") Long id,
        @RequestBody @Valid UpdateMenuItemPartialParameters body
    ) {
        MenuItem current = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified id in order to remove it"));

        if (body.name() != null && body.price() != null && body.ingredientsIds() != null) {
            current.setName(body.name());
            current.setPrice(body.price());

            List<Ingredient> ingredients = body.ingredientsIds()
                .stream()
                .map(ingredientId -> this.ingredientRepository
                    .findById(ingredientId)
                    .orElseThrow(EntityNotFoundException::new))
                .collect(Collectors.toList());

            current.setIngredients(ingredients);
        } else {
            throw new MissingResourceException(
                "All menu item fields must be informed", "Menu Item", ""
            );
        }

        MenuItem updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated Menu Item",
                MenuItemMapper.modelToResponse(updated)
            ));
    }
}
