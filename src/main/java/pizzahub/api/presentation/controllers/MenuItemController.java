package pizzahub.api.presentation.controllers;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import pizzahub.api.entities.menuitem.data.MenuItemResponseDTO;
import pizzahub.api.entities.menuitem.data.UpdateMenuItemRequestDTO;

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

        if (start >= all.size()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response(
                    "The pagination parameters are invalid. Please ensure that 'page' is smaller than the total data size",
                    null
                ));
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
        MenuItem menuItem = this.repository.findById(menuItemId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch menu item with specified ID"));

        MenuItemResponseDTO response = menuItem.convertToResponseDTO();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("Successfully fetched menu item with specified id", response));
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
                        .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve one of the ingredients informed by ID")))
                    .collect(Collectors.toList());

                newMenuItem.setIngredients(ingredients);
            } catch (EntityNotFoundException error) {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response("Failed to retrieve one of the ingredients informed by ID", null));
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
                .body(new Response("An error occured when trying to create a new menu item", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteMenuItem(@PathVariable("id") Long menuItemId) {
        MenuItem exists = this.repository.findById(menuItemId)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified id in order to remove it"));

        this.repository.deleteById(menuItemId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("Successfully deleted menu item with specified id", null));
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
                        return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new Response(
                                "Invalid 'name' parameter provided. Please ensure the value is not null and its length is greater than zero",
                                null
                            ));
                    }
                }

                if (body.price() != null) {
                    try {
                        itemMenu.setPrice(body.price());
                    } catch (Exception error) {
                        return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new Response(
                                "Invalid 'price' parameter provided. Please ensure the value is not null and its value is greater than zero",
                                null
                            ));
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
                        return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(new Response("Could not retrieve one of the ingredients with specified id", null));
                    } catch (IllegalArgumentException error) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid 'ingredients ids' list parameter provided. Please ensure the list is not null or empty", null));
                    }
                }

            } catch (Exception error) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Failed to update informed parameters", null));
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
