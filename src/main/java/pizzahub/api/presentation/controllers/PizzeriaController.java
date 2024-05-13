package pizzahub.api.presentation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.pizzeria.data.CreatePizzeriaRequestDTO;
import pizzahub.api.presentation.Response;
import pizzahub.api.repositories.PizzeriaRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/pizzeria")
public class PizzeriaController {
    @Autowired
    PizzeriaRepository repository;

    @GetMapping
    public ResponseEntity<Response> fetchPizzeria(
            @RequestParam(value = "page", defaultValue = "1") short page,
            @RequestParam(value = "perPage", defaultValue = "30") short perPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String name
    ) {

        List<Pizzeria> all = this.repository.findAll();

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

        List<Pizzeria> paginated = all.subList(start, end);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("Successfully fetched all pizzeria", paginated));
    }

    @PostMapping
    public ResponseEntity<Response> createPizzeria(@RequestBody CreatePizzeriaRequestDTO body) {
        try {
            Pizzeria pizzeria = new Pizzeria(body);
            Pizzeria createdPizzeria = this.repository.save(pizzeria);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                    "Successfully created worker",
                    createdPizzeria.convertToResponseDTO()
                ));
        }
        catch (Exception error) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Could not create pizzeria", null));
        }
    }

    @DeleteMapping
    public ResponseEntity<Response> deletePizzeria(@PathVariable(name = "code") Short code) {
        Optional<Pizzeria> pizzeriaOptional = this.repository.findById(code);

        if (pizzeriaOptional.isPresent()) {
            this.repository.deleteById(code);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully deleted pizzeria with specified id", null));
        }
        else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response("Pizzeria with specified id does not exist", null));
        }
    }


}
