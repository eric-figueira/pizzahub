package pizzahub.api.presentation.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.menuitem.MenuItem;
import pizzahub.api.entities.menuitem.data.CreateMenuItemRequestDTO;
import pizzahub.api.entities.menuitem.data.UpdateMenuItemRequestDTO;

import pizzahub.api.repositories.IngredientRepository;
import pizzahub.api.repositories.MenuItemRepository;

import pizzahub.api.presentation.Response;

@RestController
@RequestMapping(value = "/menuitems")
public class MenuItemController {

    /*
     * To Do:
     * - Pagination
     * - Order by price ?
     * - Post (must have permission)
     * - Delete (must have permission)
     * - Update (must have permission)
     * - Refactor PUT method (Ingredients Ids) to make it more efficient
    */

    @Autowired
    private MenuItemRepository repository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public ResponseEntity<List<MenuItem>> fetchMenuItems(
        @RequestParam(value = "page", defaultValue = "1") short page,
        @RequestParam(value = "perPage", defaultValue = "30") short perPage
    ) {
        List<MenuItem> all = this.repository.findAll();

        short start = (short) ((page - 1) * perPage);
        short end   = (short) (page * perPage);

        if (start >= all.size() || end >= all.size()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ArrayList<MenuItem>());
        }

        List<MenuItem> paginated = all.subList((page - 1) * perPage, page * perPage);

        return ResponseEntity.ok(paginated);
    }

    @GetMapping("/{id}")
    public Optional<MenuItem> fetchMenuItemById(@PathVariable("id") Long menuItemId) {
        return this.repository.findById(menuItemId);
    }

    @PostMapping
    public ResponseEntity createMenuItem(@RequestBody @Valid CreateMenuItemRequestDTO body) {
        // TODO: Must have permission
        try {
            MenuItem newMenuItem = new MenuItem(body);
            try {
                List<Ingredient> ingredients = body.ingredientsIds()
                    .stream()
                    .map(ingredientId -> this.ingredientRepository
                        .findById(ingredientId)
                        .orElseThrow(() -> new EntityNotFoundException()))
                    .collect(Collectors.toList());

                newMenuItem.setIngredients(ingredients);
            } catch (EntityNotFoundException error) {
                return ResponseEntity.notFound().build();
            }

            this.repository.save(newMenuItem);
            return ResponseEntity.ok(newMenuItem.getId());
        } catch (Exception error) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMenuItem(@PathVariable("id") Long menuItemId) {
        // TODO: Must have permission
        this.repository.deleteById(menuItemId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Response> updateMenuItem(@RequestBody @Valid UpdateMenuItemRequestDTO body) {
        Optional<MenuItem> optionalMenuItem = this.repository.findById(body.id());

        if (optionalMenuItem.isPresent()) {
            MenuItem itemMenu = optionalMenuItem.get();

            try {
                if (body.name() != null) {
                    try {
                        itemMenu.setName(body.name());
                    } catch (Exception error) {
                        return ResponseEntity.badRequest().body(new Response(true, "Invalid name format", null));
                    }
                }

                if (body.price() != null) {
                    try {
                        itemMenu.setPrice(body.price());
                    } catch (Exception error) {
                        return ResponseEntity.badRequest().body(new Response(true, "Invalid price format", null));
                    }
                }

                if (body.ingredientsIds() != null) {
                    try {
                        List<Ingredient> ingredients = body.ingredientsIds()
                            .stream()
                            .map(ingredientId -> this.ingredientRepository
                                .findById(ingredientId)
                                .orElseThrow(() -> new EntityNotFoundException()))
                            .collect(Collectors.toList());

                        itemMenu.setIngredients(ingredients);
                    } catch (EntityNotFoundException error) {
                        return ResponseEntity.notFound().build();
                    } catch (IllegalArgumentException error) {
                        return ResponseEntity.badRequest().body(new Response(true, "Invalid ingredients list", null));
                    }
                }

            } catch (Exception error) {
                return ResponseEntity.badRequest().build();
            }

            MenuItem updatedMenuItem = this.repository.save(itemMenu);
            return ResponseEntity.ok().body(new Response(false, "Successfully updated Menu Item", updatedMenuItem));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
