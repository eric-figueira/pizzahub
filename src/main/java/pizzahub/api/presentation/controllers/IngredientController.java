package pizzahub.api.presentation.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.ingredient.data.IngredientParameters;
import pizzahub.api.mappers.IngredientMapper;
import pizzahub.api.presentation.Response;
import pizzahub.api.repositories.IngredientRepository;
import pizzahub.api.repositories.MenuItemRepository;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    @Autowired
    private IngredientRepository repository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping
    public ResponseEntity<Response> fetchIngredients(
            @RequestParam(value = "page", defaultValue = "1") short page,
            @RequestParam(value = "perPage", defaultValue = "30") short perPage,
            @RequestParam(value = "orderByName", defaultValue = "") String order) {
        List<Ingredient> all = this.repository.findAll();

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

        List<Ingredient> paginated = all.subList(start, end);

        // ordination
        if (order.equalsIgnoreCase("desc"))
            paginated.sort(Comparator.comparing(Ingredient::getName).reversed());
        else
            paginated.sort(Comparator.comparing(Ingredient::getName));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                        "Successfully fetched all ingredients",
                        paginated
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchById(@PathVariable("id") Long ingredientId) {
        Ingredient ingredient = this.repository.findById(ingredientId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch ingredient with specified ID"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched ingredient with specified id",
                IngredientMapper.modelToResponse(ingredient)
            ));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid IngredientParameters body) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(body.name());

        Ingredient created = this.repository.save(ingredient);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully created new ingredient",
                IngredientMapper.modelToResponse(created)
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long ingredientId) {
        Ingredient ingredient = this.repository.findById(ingredientId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch ingredient with specified ID in order to remove it"));

        this.menuItemRepository.findAll()
            .stream()
            .filter(item -> item.getIngredients().contains(ingredient))
            .forEach(item -> {
                item.getIngredients().remove(ingredient);
                menuItemRepository.save(item);
            });

        this.repository.deleteById(ingredientId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully deleted ingredient with specified id", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(
        @PathVariable("id") Long ingredientId,
        @RequestBody @Valid IngredientParameters body
    ) {
        Ingredient current = this.repository.findById(ingredientId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch ingredient with specified ID in order to update it"));

        if (body.name() != null) current.setName(body.name());
        else {
            throw new MissingResourceException(
                "All ingredient fields must be informed", "Ingredient", ""
            );
        }

        Ingredient updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated ingredient",
                IngredientMapper.modelToResponse(updated)
            ));
    }
}
