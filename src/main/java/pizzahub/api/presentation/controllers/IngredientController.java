package pizzahub.api.presentation.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import pizzahub.api.repositories.IngredientRepository;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    @Autowired
    private IngredientRepository repository;

    @GetMapping
    public ResponseEntity<List<Ingredient>> fetchIngredients (
        @RequestParam(value = "page", defaultValue = "1") short page,
        @RequestParam(value = "perPage", defaultValue = "30") short perPage
    ) {
        List<Ingredient> all = this.repository.findAll();

        short start = (short) ((page - 1) * perPage);
        short end   = (short) (page * perPage);

        if (start >= all.size() || end >= all.size()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ArrayList<Ingredient>());
        }

        List<Ingredient> paginated = all.subList((page - 1) * perPage, page * perPage);

        return ResponseEntity.ok(paginated);
    }

    @GetMapping("/{id}")
    public Optional<Ingredient> fetchIngredientById(@PathVariable("id") Long ingredientId) {
        return this.repository.findById(ingredientId);
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
