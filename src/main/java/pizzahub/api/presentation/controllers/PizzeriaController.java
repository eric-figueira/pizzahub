package pizzahub.api.presentation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.pizzeria.data.CreatePizzeriaRequestDTO;
import pizzahub.api.entities.pizzeria.data.FetchPizzeriaResponseDTO;
import pizzahub.api.entities.pizzeria.data.UpdatePizzeriaRequestDTO;
import pizzahub.api.infrastructure.cep.Address;
import pizzahub.api.infrastructure.cep.ViaCepClient;
import pizzahub.api.presentation.Response;
import pizzahub.api.repositories.PizzeriaRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/pizzerias")
public class PizzeriaController {
    @Autowired
    PizzeriaRepository repository;

    @Autowired
    private ViaCepClient cepClient;

    @GetMapping
    public ResponseEntity<Response> fetchPizzerias(
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

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchPizzeriaByCode(@PathVariable("id") Short pizzeriaId) {
        Optional<Pizzeria> pizzeriaOptional = this.repository.findByCode(pizzeriaId);

        if (pizzeriaOptional.isPresent()) {
            Pizzeria pizzeria = pizzeriaOptional.get();

            FetchPizzeriaResponseDTO response = pizzeria.convertToResponseDTO();

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully fetched pizzeria with specified id", response));
        }
        else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response("Could not fetch pizzeria with specified ID", null));
        }
    }

    @PostMapping
    public ResponseEntity<Response> createPizzeria(@RequestBody CreatePizzeriaRequestDTO body) {
        try {
            Pizzeria pizzeria = new Pizzeria(body);

            String cep = body.cep();

            Address address = cepClient.fetchAddressByCep(cep);
            pizzeria.setCity(address.getLocalidade());
            pizzeria.setNeighborhood(address.getBairro());
            pizzeria.setUf(address.getUf());
            pizzeria.setStreetName(address.getLogradouro());

            Pizzeria savedPizzeria = this.repository.save(pizzeria);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                    "Successfully created new pizzeria",
                    savedPizzeria.convertToResponseDTO()
                ));
        }
        catch (Exception error) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("An error occured when trying to create a new pizzeria", null));
        }
    }

    @DeleteMapping
    public ResponseEntity<Response> deletePizzeria(@PathVariable(name = "code") Short code) {
        Optional<Pizzeria> pizzeriaOptional = this.repository.findByCode(code);

        if (pizzeriaOptional.isPresent()) {
            this.repository.deleteById(code);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully deleted pizzeria with specified id", null));
        }
        else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response("Could not retrieve pizzeria with specified id in order to remove it", null));
        }
    }

    @PutMapping
    public ResponseEntity<Response> updatePizzeria(@RequestBody @Valid UpdatePizzeriaRequestDTO body) {
        Optional<Pizzeria> optionalPizzeria = this.repository.findByCode(body.code());

        if (optionalPizzeria.isPresent()) {
            Pizzeria pizzeria = optionalPizzeria.get();

            try {
                if (body.firstContact() != null) {
                    try {
                        pizzeria.setFirstContact(body.firstContact());
                    } catch (Exception error) {
                        return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new Response(
                                "Invalid 'first contact' parameter provided. Please ensure the value is not null and its length is greater than zero",
                                null
                            ));
                    }
                }

                if (body.secondContact() != null) {
                    try {
                        pizzeria.setSecondContact(body.secondContact());
                    } catch (Exception error) {
                        return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new Response(
                                "Invalid 'second contact' parameter provided. Please ensure the value is not null and its length is greater than zero",
                                null
                            ));
                    }
                }

                if (body.email() != null) {
                    try {
                        pizzeria.setEmail(body.email());
                    } catch (Exception error) {
                        return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new Response(
                                "Invalid 'email' parameter provided. Please ensure the value is not null and its length is greater than zero",
                                null
                            ));
                    }
                }

                if (body.cep() != null) {
                    try {
                        String cep = body.cep();

                        Address address = cepClient.fetchAddressByCep(cep);
                        pizzeria.setCity(address.getLocalidade());
                        pizzeria.setNeighborhood(address.getBairro());
                        pizzeria.setUf(address.getUf());
                        pizzeria.setStreetName(address.getLogradouro());
                        pizzeria.setCep(body.cep());

                    } catch (Exception error) {
                        return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new Response(
                                "Invalid 'CEP' parameter provided. Please ensure the value is not null and its length is greater than zero",
                                null
                            ));
                    }
                }

                if (body.complement() != null) {
                    try {
                        pizzeria.setComplement(body.complement());
                    } catch (Exception error) {
                        return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new Response(
                                "Invalid 'complement' parameter provided. Please ensure the value is not null and its length is greater than zero",
                                null
                            ));
                    }
                }

                if (body.addressNumber() != null) {
                    try {
                        pizzeria.setAddressNumber(body.addressNumber());
                    } catch (Exception error) {
                        return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new Response(
                                "Invalid 'address number' parameter provided. Please ensure the value is not null and its length is greater than zero",
                                null
                            ));
                    }
                }

            } catch (Exception error) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Failed to retrieve informed parameters", null));
            }

            Pizzeria updatedPizzeria = this.repository.save(pizzeria);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                    "Successfully updated pizzeria",
                    updatedPizzeria.convertToResponseDTO()
                ));
        }
        else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response(
                    "Failed to update pizzeria",
                    null
                ));
        }
    }
}
