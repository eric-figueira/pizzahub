package pizzahub.api.presentation.controllers;

import java.util.*;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.pizzeria.data.SavePizzeriaParameters;
import pizzahub.api.entities.pizzeria.data.UpdatePizzeriaPartialParameters;
import pizzahub.api.entities.user.worker.Worker;
import pizzahub.api.entities.user.worker.data.SaveWorkerParameters;
import pizzahub.api.entities.user.worker.data.UpdateWorkerPartialParameters;
import pizzahub.api.infrastructure.cep.Address;
import pizzahub.api.infrastructure.cep.ViaCepClient;
import pizzahub.api.mappers.PizzeriaMapper;
import pizzahub.api.mappers.WorkerMapper;
import pizzahub.api.presentation.Response;
import pizzahub.api.repositories.PizzeriaRepository;
import pizzahub.api.repositories.WorkerRepository;

@RestController
@RequestMapping("/pizzerias")
public class PizzeriaController {

    @Autowired private PizzeriaRepository repository;
    @Autowired private WorkerRepository workerRepository;
    @Autowired private PizzeriaMapper mapper;
    @Autowired private WorkerMapper workerMapper;
    @Autowired private ViaCepClient cepClient;

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
                this.mapper.fromEntityListToResponseList(paginated)
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
                this.mapper.fromEntityToResponse(pizzeria)
            ));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid SavePizzeriaParameters body) {
        Pizzeria pizzeria = this.mapper.fromSaveParametersToEntity(body);

        String cep = body.cep();

        Address address = cepClient.fetchAddressByCep(cep);
        pizzeria.setCity(address.getCidade());
        pizzeria.setNeighborhood(address.getBairro());
        pizzeria.setUf(address.getEstado());
        pizzeria.setStreetName(address.getLogradouro());

        if (body.workersIds() != null && body.workersIds().size() != 0) {
            List<Worker> workers = body.workersIds()
                .stream()
                .map(workerId -> this.workerRepository
                    .findById(workerId)
                    .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve one of the workers informed by ID")))
                .toList();

            pizzeria.setWorkers(workers);
        }

        Pizzeria created = this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully created new pizzeria",
                this.mapper.fromEntityToResponse(created)
            ));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Response> delete(@PathVariable(name = "code") Short code) {
        Pizzeria exists = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code in order to remove it"));

        this.repository.deleteById(exists.getId());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("Successfully deleted pizzeria with specified code", null));
    }

    @PatchMapping("/{code}")
    public ResponseEntity<Response> updatePartial(
        @PathVariable("code") Short code,
        @RequestBody @Valid UpdatePizzeriaPartialParameters body
    ) {
        Pizzeria pizzeria = this.repository.findByCode(code)
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

        if (body.workersIds() != null && body.workersIds().size() != 0) {
            List<Worker> workers = body.workersIds()
                .stream()
                .map(workerId -> this.workerRepository
                    .findById(workerId)
                    .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve one of the workers informed by ID")))
                .toList();

            pizzeria.setWorkers(workers);
        }

        Pizzeria updated = this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated pizzeria",
                this.mapper.fromEntityToResponse(updated)
            ));
    }

    @PutMapping("/{code}")
    public ResponseEntity<Response> update(
        @PathVariable("code") Short code,
        @RequestBody @Valid SavePizzeriaParameters body
    ) {
        Pizzeria pizzeria = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code in order to update it"));

        this.mapper.updatePizzeria(pizzeria, body);

        String cep = body.cep();

        Address address = cepClient.fetchAddressByCep(cep);

        pizzeria.setCity(address.getCidade());
        pizzeria.setNeighborhood(address.getBairro());
        pizzeria.setUf(address.getEstado());
        pizzeria.setStreetName(address.getLogradouro());
        pizzeria.setCep(body.cep());

        List<Worker> workers = body.workersIds()
            .stream()
            .map(workerId -> this.workerRepository
                .findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve one of the workers informed by ID")))
            .toList();

        pizzeria.setWorkers(workers);

        Pizzeria updated = this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated pizzeria",
                this.mapper.fromEntityToResponse(updated)
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
                pizzeria.getWorkers().stream().map(worker -> this.workerMapper.fromEntityToResponse(worker))
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

    @PostMapping("/{code}/workers/{workerId}")
    public ResponseEntity<Response> hireWorker(
        @PathVariable("code") Short code,
        @PathVariable("workerId") UUID workerId
    ) {
        Pizzeria pizzeria = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code"));

        Worker worker = this.workerRepository.findById(workerId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch worker with specified id"));

        if (pizzeria.getWorkers().contains(worker))
            throw new IllegalArgumentException("Worker is already working in that pizzeria");

        pizzeria.getWorkers().add(worker);
        worker.setPizzeria(pizzeria);

        this.workerRepository.save(worker);
        this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully added worker with specified id to pizzeria",
                null
            ));
    }

    @DeleteMapping("/{code}/workers/{workerId}")
    public ResponseEntity<Response> fireWorker(
        @PathVariable("code") Short code,
        @PathVariable("workerId") UUID workerId
    ) {
        Pizzeria pizzeria = this.repository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code"));

        Worker worker = this.workerRepository.findById(workerId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch worker with specified id"));

        if (!pizzeria.getWorkers().contains(worker))
            throw new NoSuchElementException("Worker does not exist in pizzeria informed");

        pizzeria.getWorkers().remove(worker);
        worker.setPizzeria(null);

        this.workerRepository.save(worker);
        this.repository.save(pizzeria);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully removed worker with specified id from pizzeria",
                null
            ));
    }
}
