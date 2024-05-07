package pizzahub.api.presentation.controllers;

import java.util.Comparator;
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
import pizzahub.api.entities.menuitem.data.FetchMenuItemsResponseDTO;
import pizzahub.api.entities.menuitem.data.UpdateMenuItemRequestDTO;

import pizzahub.api.repositories.IngredientRepository;
import pizzahub.api.repositories.MenuItemRepository;

import pizzahub.api.presentation.Response;

@RestController
@RequestMapping(value = "/menuitems")
public class MenuItemController {

    /*
     * To Do:
     * [x] Pagination
     * [x] Order by price
     * [] Post (must have permission)
     * [] Delete (must have permission)
     * [] Update (must have permission)
     * [] Refactor PUT method (Ingredients Ids) to make it more efficient
    */

    @Autowired
    private MenuItemRepository repository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public ResponseEntity<Response> fetchMenuItems(
        @RequestParam(value = "page", defaultValue = "1") short page,
        @RequestParam(value = "perPage", defaultValue = "30") short perPage,
        @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
        @RequestParam(value = "order", defaultValue = "asc") String order
    ) {
        List<MenuItem> all = this.repository.findAll();

        // pagination
        short start = (short) ((page - 1) * perPage);
        short end = 1;

        double numberOfGroups = (double) all.size() / perPage;
        short lastGroupNumber = (short) Math.ceil(numberOfGroups);

        if (page == lastGroupNumber) {
            // pagination refers to last page
            end = (short) all.size();
        } else {
            end = (short) (page * perPage);
        }

        if (start >= all.size() || end > all.size()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response("Invalid pagination parameters", null));
        }

        List<MenuItem> paginated = all.subList(start, end);

        // ordenation
        switch (orderBy) {
            case "price":
                if (order.equalsIgnoreCase("asc"))
                    paginated.sort(Comparator.comparing(MenuItem::getPrice));
                else
                    paginated.sort(Comparator.comparing(MenuItem::getPrice).reversed());
                break;

            default:
                break;
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched all menu items",
                paginated.stream()
                    .map(item -> item.convertToResponseDTO())
                    .collect(Collectors.toList())
            ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchMenuItemById(@PathVariable("id") Long menuItemId) {
        Optional<MenuItem> menuItemOptional = this.repository.findById(menuItemId);

        if (menuItemOptional.isPresent()) {
            MenuItem menuItem = menuItemOptional.get();

            FetchMenuItemsResponseDTO response = menuItem.convertToResponseDTO();

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully fetched menu item with specified id", response));
        }
        else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response("Could not found menu item with specified id", null));
        }
    }

    @PostMapping
    public ResponseEntity<Response> createMenuItem(@RequestBody @Valid CreateMenuItemRequestDTO body) {
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
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response("Could not found ingredient", null));
            }

            MenuItem savedMenuItem = this.repository.save(newMenuItem);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                    "Successfully created new menu item",
                    savedMenuItem.convertToResponseDTO()
                ));
        }
        catch (Exception error) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("Could not create new menu item", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteMenuItem(@PathVariable("id") Long menuItemId) {
        // TODO: Must have permission
        Optional<MenuItem> menuItemOptional = this.repository.findById(menuItemId);

        if (menuItemOptional.isPresent()) {
            this.repository.deleteById(menuItemId);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully deleted menu item with specified id", null));
        }
        else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response("Menu item with specified id does not exist", null));
        }
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
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid name format", null));
                    }
                }

                if (body.price() != null) {
                    try {
                        itemMenu.setPrice(body.price());
                    } catch (Exception error) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid price format", null));
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
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid ingredients list", null));
                    }
                }

            } catch (Exception error) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Failed to retrieve informed parameters", null));
            }

            MenuItem updatedMenuItem = this.repository.save(itemMenu);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                    "Successfully updated Menu Item",
                    updatedMenuItem.convertToResponseDTO()
                ));
        }
        else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response(
                    "Failed to update menu item",
                    null
                ));
        }
    }
}
