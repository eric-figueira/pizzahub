package pizzahub.api.presentation.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.MissingResourceException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.ingredient.data.SaveIngredientParameters;
import pizzahub.api.entities.ingredient.data.UpdateIngredientPartialParameters;
import pizzahub.api.mappers.IngredientMapper;
import pizzahub.api.presentation.Response;
import pizzahub.api.repositories.IngredientRepository;
import pizzahub.api.repositories.MenuItemRepository;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    @Autowired private IngredientRepository repository;
    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private IngredientMapper mapper;

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
                        this.mapper.fromEntityListToResponseList(paginated)
                ));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Response> fetchBySlug(@PathVariable("slug") String slug) {
        Ingredient ingredient = this.repository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch ingredient with specified slug"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched ingredient with specified slug",
                this.mapper.fromEntityToResponse(ingredient)
            ));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid SaveIngredientParameters body) {
        Ingredient ingredient = this.mapper.fromSaveParametersToEntity(body);

        Ingredient created = this.repository.save(ingredient);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully created new ingredient",
                this.mapper.fromEntityToResponse(created)
            ));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Response> delete(@PathVariable("slug") String slug) {
        Ingredient ingredient = this.repository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Could not find ingredient with specified slug in order to delete it"));

        this.menuItemRepository.findAll()
            .stream()
            .filter(item -> item.getIngredients().contains(ingredient))
            .forEach(item -> {
                item.getIngredients().remove(ingredient);
                menuItemRepository.save(item);
            });

        this.repository.deleteById(ingredient.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully deleted ingredient with specified slug", null));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Response> update(
        @PathVariable("slug") String slug,
        @RequestBody @Valid SaveIngredientParameters body
    ) {
        Ingredient current = this.repository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Could not find ingredient with specified slug in order to update it"));

        this.mapper.updateIngredient(current, body);

        Ingredient updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated ingredient",
                this.mapper.fromEntityToResponse(updated)
            ));
    }

    @PatchMapping("/{slug}")
    public ResponseEntity<Response> updatePartial(
        @PathVariable("slug") String slug,
        @RequestBody @Valid UpdateIngredientPartialParameters body
    ) {
        Ingredient current = this.repository.findBySlug(slug)
            .orElseThrow(() -> new EntityNotFoundException("Could not find ingredient with specified slug in order to update it"));

        if (body.slug() != null) current.setSlug(body.slug());
        if (body.name() != null) current.setName(body.name());

        Ingredient updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated ingredient",
                this.mapper.fromEntityToResponse(updated)
            ));
    }
}
