package pizzahub.api.presentation.controllers;

import java.nio.channels.AlreadyConnectedException;
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
import pizzahub.api.entities.menuitem.data.SaveMenuItemParameters;
import pizzahub.api.entities.menuitem.data.UpdateMenuItemPartialParameters;

import pizzahub.api.mappers.MenuItemMapper;
import pizzahub.api.repositories.IngredientRepository;
import pizzahub.api.repositories.MenuItemRepository;

import pizzahub.api.presentation.Response;

@RestController
@RequestMapping(value = "/menuitems")
public class MenuItemController {

    @Autowired private MenuItemRepository repository;
    @Autowired private IngredientRepository ingredientRepository;
    @Autowired private MenuItemMapper mapper;

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
                this.mapper.fromEntityListToResponseList(paginated)
            ));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Response> fetchBySlug(@PathVariable("slug") String slug) {
        MenuItem menuItem = this.repository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch menu item with specified slug"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched menu item with specified slug",
                this.mapper.fromEntityToResponse(menuItem)
            ));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid SaveMenuItemParameters body) {
        MenuItem menuItem = this.mapper.fromSaveParametersToEntity(body);

        List<Ingredient> ingredients = body.ingredientsIds()
            .stream()
            .map(ingredientId -> this.ingredientRepository
                .findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve one of the ingredients informed by ID")))
            .collect(Collectors.toList());

        menuItem.setIngredients(ingredients);

        MenuItem created = this.repository.save(menuItem);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully created new menu item",
                this.mapper.fromEntityToResponse(created)
            ));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Response> delete(@PathVariable("slug") String slug) {
        MenuItem exists = this.repository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified slug in order to remove it"));

        this.repository.deleteById(exists.getId());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("Successfully deleted menu item with specified slug", null));
    }

    @PatchMapping("/{slug}")
    public ResponseEntity<Response> updatePartial(
        @PathVariable("slug") String slug,
        @RequestBody @Valid UpdateMenuItemPartialParameters body
    ) {
        MenuItem current = this.repository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified slug in order to update it"));

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
                this.mapper.fromEntityToResponse(updated)
            ));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Response> update(
        @PathVariable("slug") String slug,
        @RequestBody @Valid SaveMenuItemParameters body
    ) {
        MenuItem current = this.repository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified id in order to update it"));

        this.mapper.updateMenuItem(current, body);

        List<Ingredient> ingredients = body.ingredientsIds()
            .stream()
            .map(ingredientId -> this.ingredientRepository
                .findById(ingredientId)
                .orElseThrow(EntityNotFoundException::new))
            .collect(Collectors.toList());

        current.setIngredients(ingredients);

        MenuItem updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated Menu Item",
                this.mapper.fromEntityToResponse(updated)
            ));
    }

    @PatchMapping("{slug}/ingredients/{ingredientSlug}")
    public ResponseEntity<Response> addIngredient(
        @PathVariable("slug") String slug,
        @PathVariable("ingredientSlug") String ingredientSlug
    ) {
        MenuItem menuItem = this.repository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified slug in order to add the ingredient"));

        Ingredient ingredient = this.ingredientRepository.findBySlug(ingredientSlug)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve ingredient with specified slug in order to add it to the menu item"));

        if (menuItem.getIngredients().contains(ingredient))
            throw new IllegalArgumentException("Menu item already contains specified ingredient");

        menuItem.getIngredients().add(ingredient);
        MenuItem updated = this.repository.save(menuItem);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully added ingredient to menu item",
                this.mapper.fromEntityToResponse(updated)
            ));
    }

    @DeleteMapping("{slug}/ingredients/{ingredientSlug}")
    public ResponseEntity<Response> removeIngredient(
        @PathVariable("slug") String slug,
        @PathVariable("ingredientSlug") String ingredientSlug
    ) {
        MenuItem menuItem = this.repository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified slug in order to remove the ingredient"));

        Ingredient ingredient = this.ingredientRepository.findBySlug(ingredientSlug)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve ingredient with specified slug in order to remove it from the menu item"));

        if (!menuItem.getIngredients().contains(ingredient))
            throw new NoSuchElementException("Menu item does not contain specified ingredient");

        menuItem.getIngredients().remove(ingredient);
        MenuItem updated = this.repository.save(menuItem);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully removed ingredient from menu item",
                this.mapper.fromEntityToResponse(updated)
            ));
    }
}
