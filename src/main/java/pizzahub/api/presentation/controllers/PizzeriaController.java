package pizzahub.api.presentation.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.pizzeria.data.CreatePizzeriaParameters;
import pizzahub.api.entities.pizzeria.data.UpdatePizzeriaParameters;
import pizzahub.api.entities.pizzeria.data.UpdatePizzeriaPartialParameters;
import pizzahub.api.infrastructure.cep.Address;
import pizzahub.api.infrastructure.cep.ViaCepClient;
import pizzahub.api.mappers.PizzeriaMapper;
import pizzahub.api.presentation.Response;
import pizzahub.api.repositories.PizzeriaRepository;

@RestController
@RequestMapping("/pizzerias")
public class PizzeriaController {
    @Autowired
    PizzeriaRepository repository;

    @Autowired
    private ViaCepClient cepClient;

    @GetMapping
    public ResponseEntity<Response> fetchAll(
            @RequestParam(value = "page", defaultValue = "1") short page,
            @RequestParam(value = "perPage", defaultValue = "30") short perPage,
            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
            @RequestParam(value = "order", defaultValue = "asc") String order
    ) {

        List<Pizzeria> all = this.repository.findAll();

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

        List<Pizzeria> paginated = all.subList(start, end);

        if ("code".equals(orderBy)) {
            if (order.equalsIgnoreCase("asc"))
                paginated.sort(Comparator.comparing(Pizzeria::getCode));
            else
                paginated.sort(Comparator.comparing(Pizzeria::getCode).reversed());
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched all pizzeria",
                paginated
            ));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Response> fetchByCode(@PathVariable("code") Short code) {
        Pizzeria pizzeria = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched pizzeria with specified id",
                PizzeriaMapper.modelToResponse(pizzeria)
            ));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid CreatePizzeriaParameters body) {
        Pizzeria pizzeria = new Pizzeria();
        pizzeria.setAddressNumber(body.addressNumber());
        pizzeria.setCep(body.cep());
        pizzeria.setCode(body.code());
        pizzeria.setEmail(body.email());
        pizzeria.setFirstContact(body.firstContact());
        pizzeria.setComplement(body.complement());
        pizzeria.setSecondContact(body.secondContact());

        String cep = body.cep();

        Address address = cepClient.fetchAddressByCep(cep);
        System.out.println(address.toString());
        pizzeria.setCity(address.getCidade());
        System.out.println(address.getBairro());
        pizzeria.setNeighborhood(address.getBairro());
        pizzeria.setUf(address.getEstado());
        pizzeria.setStreetName(address.getLogradouro());

        Pizzeria created = this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully created new pizzeria",
                PizzeriaMapper.modelToResponse(created)
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable(name = "id") Long id) {
        Pizzeria exists = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code in order to remove it"));

        this.repository.deleteById(id);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("Successfully deleted pizzeria with specified id", null));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updatePartial(
        @PathVariable("id") Long pizzeriaId,
        @RequestBody @Valid UpdatePizzeriaPartialParameters body
    ) {
        Pizzeria pizzeria = this.repository.findById(pizzeriaId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code in order to update it"));

        if (body.firstContact() != null) pizzeria.setFirstContact(body.firstContact());
        if (body.secondContact() != null) pizzeria.setSecondContact(body.secondContact());
        if (body.email() != null) pizzeria.setEmail(body.email());

        if (body.cep() != null) {
            String cep = body.cep();

            Address address = cepClient.fetchAddressByCep(cep);

            pizzeria.setCity(address.getCidade());
            pizzeria.setNeighborhood(address.getBairro());
            pizzeria.setUf(address.getEstado());
            pizzeria.setStreetName(address.getLogradouro());
            pizzeria.setCep(body.cep());
        }

        if (body.complement() != null) pizzeria.setComplement(body.complement());
        if (body.addressNumber() != null) pizzeria.setAddressNumber(body.addressNumber());

        Pizzeria updated = this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated pizzeria",
                PizzeriaMapper.modelToResponse(updated)
            ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(
        @PathVariable("id") Long pizzeriaId,
        @RequestBody @Valid UpdatePizzeriaParameters body
    ) {
        Pizzeria pizzeria = this.repository.findById(pizzeriaId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code in order to update it"));

        if (body.code() != null && body.firstContact() != null && body.email() != null &&
            body.cep() != null && body.addressNumber() != null) {

            pizzeria.setFirstContact(body.firstContact());
            if (body.secondContact() != null)
                pizzeria.setSecondContact(body.secondContact());

            pizzeria.setEmail(body.email());

            String cep = body.cep();

            Address address = cepClient.fetchAddressByCep(cep);

            pizzeria.setCity(address.getCidade());
            pizzeria.setNeighborhood(address.getBairro());
            pizzeria.setUf(address.getEstado());
            pizzeria.setStreetName(address.getLogradouro());
            pizzeria.setCep(body.cep());

            if (body.complement() != null)
                pizzeria.setComplement(body.complement());
            pizzeria.setAddressNumber(body.addressNumber());
        }

        Pizzeria updated = this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated pizzeria",
                PizzeriaMapper.modelToResponse(updated)
            ));
    }
}
