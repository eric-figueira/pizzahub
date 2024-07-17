package pizzahub.api.presentation.controllers;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import pizzahub.api.entities.ingredient.Ingredient;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.pizzeria.data.CreatePizzeriaParameters;
import pizzahub.api.entities.pizzeria.data.UpdatePizzeriaParameters;
import pizzahub.api.entities.pizzeria.data.UpdatePizzeriaPartialParameters;
import pizzahub.api.entities.user.Role;
import pizzahub.api.entities.user.worker.Worker;
import pizzahub.api.entities.user.worker.data.CreateWorkerParameters;
import pizzahub.api.entities.user.worker.data.UpdateWorkerParameters;
import pizzahub.api.entities.user.worker.data.UpdateWorkerPartialParameters;
import pizzahub.api.infrastructure.cep.Address;
import pizzahub.api.infrastructure.cep.ViaCepClient;
import pizzahub.api.mappers.MenuItemMapper;
import pizzahub.api.mappers.PizzeriaMapper;
import pizzahub.api.mappers.WorkerMapper;
import pizzahub.api.presentation.Response;
import pizzahub.api.repositories.PizzeriaRepository;
import pizzahub.api.repositories.WorkerRepository;

@RestController
@RequestMapping("/pizzerias")
public class PizzeriaController {
    @Autowired
    PizzeriaRepository repository;

    @Autowired
    WorkerRepository workerRepository;

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

        if (body.workersIds() != null || body.workersIds().size() != 0) {
            List<Worker> workers = body.workersIds()
                .stream()
                .map(workerId -> this.workerRepository
                    .findById(workerId)
                    .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve one of the workers informed by ID")))
                .toList();

            pizzeria.setWorkersList(workers);
        }

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

        if (body.workersIds() != null || body.workersIds().size() != 0) {
            List<Worker> workers = body.workersIds()
                .stream()
                .map(workerId -> this.workerRepository
                    .findById(workerId)
                    .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve one of the workers informed by ID")))
                .toList();

            pizzeria.setWorkersList(workers);
        }

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
            body.cep() != null && body.addressNumber() != null && body.complement() != null
            && body.workersIds() != null && body.workersIds().size() != 0) {

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

            pizzeria.setComplement(body.complement());
            pizzeria.setAddressNumber(body.addressNumber());

            List<Worker> workers = body.workersIds()
                .stream()
                .map(workerId -> this.workerRepository
                    .findById(workerId)
                    .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve one of the workers informed by ID")))
                .toList();

            pizzeria.setWorkersList(workers);

        } else {
            throw new MissingResourceException(
                "All pizzeria fields must be informed", "Pizzeria", ""
            );
        }

        Pizzeria updated = this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated pizzeria",
                PizzeriaMapper.modelToResponse(updated)
            ));
    }

    @GetMapping("/{code}/workers")
    public ResponseEntity<Response> fetchWorkers(@PathVariable("code") Short code) {
        Pizzeria pizzeria = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched workers from the pizzeria with specified id",
                pizzeria.getWorkers().stream().map(WorkerMapper::modelToResponse)
            ));
    }

    @GetMapping("/{code}/workers/{workerId}")
    public ResponseEntity<Response> fetchWorkerById(
        @PathVariable("code") Short code,
        @PathVariable("workerId") Long workerId
    ) {
        Pizzeria pizzeria = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched worker with specified id in pizzeria",
                pizzeria.getWorkers().stream().filter(worker -> Objects.equals(worker.getId(), workerId))
            ));
    }

    @PostMapping("/{code}/workers")
    public ResponseEntity<Response> addWorker(
        @PathVariable("code") Short code,
        @RequestBody @Valid CreateWorkerParameters worker
    ) {
        Pizzeria pizzeria = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code"));

        this.workerRepository.save(WorkerMapper.createParametersToModel(worker));

        pizzeria.getWorkers().add(WorkerMapper.createParametersToModel(worker));

        this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully added worker with specified id to pizzeria",
                null
            ));
    }

    @DeleteMapping("/{code}/workers/{workerId}")
    public ResponseEntity<Response> removeWorker(
        @PathVariable("code") Short code,
        @PathVariable("workerId") Long workerId
    ) {
        Pizzeria pizzeria = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code"));

        Worker worker = this.workerRepository.findById(workerId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch worker with specified id"));

        if (pizzeria.getWorkers().contains(worker)) {
            pizzeria.getWorkers().remove(worker);
        } else {
            throw new NoSuchElementException("Worker does not exist in pizzeria informed");
        }

        this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully removed worker with specified id from pizzeria",
                null
            ));
    }

    @PutMapping("/{code}/workers/{workerId}")
    public ResponseEntity<Response> updateWorker(
        @PathVariable("code") Short code,
        @PathVariable("workerId") Long workerId,
        @RequestBody @Valid UpdateWorkerParameters body
    ) {
        Pizzeria pizzeria = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code"));

        Worker current = this.workerRepository.findById(workerId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch worker with specified id"));

        if (!pizzeria.getWorkers().contains(current)) {
            throw new NoSuchElementException("Worker does not exist in pizzeria informed");
        }

        if (body.email() != null && body.fullname() != null && body.password() != null && body.role() != null) {
            current.setEmail(body.email());
            current.setFullname(body.fullname());
            current.setPassword(body.password());
            current.setRole(body.role());
        } else {
            throw new MissingResourceException(
                "All worker fields must be informed", "Menu Item", ""
            );
        }

        Worker updated = this.workerRepository.save(current);

        // ?
        // pizzeria.getWorkers().set(pizzeria.getWorkers().indexOf(current), updated);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated worker",
                WorkerMapper.modelToResponse(updated)
            ));
    }

    @PatchMapping("/{code}/workers/{workerId}")
    public ResponseEntity<Response> updateWorker(
        @PathVariable("code") Short code,
        @PathVariable("workerId") Long workerId,
        @RequestBody @Valid UpdateWorkerPartialParameters body
    ) {
        Pizzeria pizzeria = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code"));

        Worker current = this.workerRepository.findById(workerId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch worker with specified id"));

        if (!pizzeria.getWorkers().contains(current)) {
            throw new NoSuchElementException("Worker does not exist in pizzeria informed");
        }

        if (body.email() != null) current.setEmail(body.email());
        if (body.fullname() != null) current.setFullname(body.fullname());
        if (body.password() != null) current.setPassword(body.password());
        if (body.role() != null) current.setRole(body.role());

        Worker updated = this.workerRepository.save(current);

        // ?
        // pizzeria.getWorkers().set(pizzeria.getWorkers().indexOf(current), updated);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated worker",
                WorkerMapper.modelToResponse(updated)
            ));
    }
}
