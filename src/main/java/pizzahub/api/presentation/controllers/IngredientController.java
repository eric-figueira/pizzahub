package pizzahub.api.presentation.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.ingredient.data.CreateIngredientRequestDTO;
import pizzahub.api.entities.ingredient.data.FetchIngredientsResponseDTO;
import pizzahub.api.presentation.Response;
import pizzahub.api.repositories.IngredientRepository;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    @Autowired
    private IngredientRepository repository;

    @GetMapping
    public ResponseEntity<Response> fetchIngredients (
        @RequestParam(value = "page", defaultValue = "1") short page,
        @RequestParam(value = "perPage", defaultValue = "30") short perPage,
        @RequestParam(value = "orderByCount", defaultValue = "asc") String order
    ) {
        List<Ingredient> all = this.repository.findAll();

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

        List<Ingredient> paginated = all.subList(start, end);

        switch (order) {
            case "desc":
                paginated.sort(Comparator.comparingInt(item -> ((Ingredient) item).getMenuItems().size()).reversed());
                break;

            default:
                paginated.sort(Comparator.comparingInt(item -> ((Ingredient) item).getMenuItems().size()));
                break;
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched all ingredients",
                paginated.stream()
                    .map(ingredient -> ingredient.convertToResponseDTO())
                    .collect(Collectors.toList())
            ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchIngredientById(@PathVariable("id") Long ingredientId) {
        Optional<Ingredient> optionalIngredient = this.repository.findById(ingredientId);

        if (optionalIngredient.isPresent()) {
            Ingredient menuItem = optionalIngredient.get();

            FetchIngredientsResponseDTO response = menuItem.convertToResponseDTO();

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                    "Successfully fetched ingredient with specified id",
                    response
                ));
        }
        else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response(
                    "Could not find ingredient with specified id",
                    null
                ));
        }
    }

    @PostMapping
    public ResponseEntity<Long> createIngredient(@RequestBody @Valid CreateIngredientRequestDTO body) {
        try {
            // TODO: Must have permission
            Ingredient newIngredient = new Ingredient(body);

            this.repository.save(newIngredient);
            return ResponseEntity.ok(newIngredient.getId());
        } catch (Exception error) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
