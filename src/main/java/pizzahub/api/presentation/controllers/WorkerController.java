package pizzahub.api.presentation.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.user.worker.Worker;
import pizzahub.api.entities.user.worker.data.UpdateWorkerPartialParameters;
import pizzahub.api.entities.user.worker.data.SaveWorkerParameters;
import pizzahub.api.mappers.WorkerMapper;
import pizzahub.api.repositories.PizzeriaRepository;
import pizzahub.api.repositories.WorkerRepository;
import pizzahub.api.presentation.Response;

@RestController
@RequestMapping("/workers")
public class WorkerController {

    @Autowired WorkerRepository repository;
    @Autowired PizzeriaRepository pizzeriaRepository;
    @Autowired WorkerMapper mapper;

    @GetMapping
    public ResponseEntity<Response> fetchAll(
        @RequestParam(value = "page", defaultValue = "1") short page,
        @RequestParam(value = "perPage", defaultValue = "30") short perPage
    ) {
        List<Worker> all = this.repository.findAll();

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

        List<Worker> paginated = all.subList(start, end);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched all workers",
                this.mapper.fromEntityListToResponseList(paginated)
            ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchById(@PathVariable("id") UUID id) {
        Worker worker = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not find worker with specified id"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched worker with specified id",
                this.mapper.fromEntityToResponse(worker)));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid SaveWorkerParameters body) {
        Worker worker = this.mapper.fromSaveParametersToEntity(body);

        Pizzeria pizzeria = this.pizzeriaRepository.findByCode(body.pizzeriaCode())
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve pizzeria with specified code"));

        worker.setCreatedAt(new Date());
        worker.setPizzeria(pizzeria);

        Worker created = this.repository.save(worker);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully created worker",
               this.mapper.fromEntityToResponse(created)
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") UUID id) {
        Worker exists = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not find worker with specified id"));

        this.repository.deleteById(id);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("Successfully deleted worker with specified id", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(
        @PathVariable("id") UUID id,
        @RequestBody @Valid SaveWorkerParameters body
    ) {
        Worker current = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch worker with specified id"));

        Pizzeria pizzeria = this.pizzeriaRepository.findByCode(body.pizzeriaCode())
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve pizzeria with specified code"));

        this.mapper.updateWorker(current, body);
        current.setPizzeria(pizzeria);

        Worker updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated worker",
                this.mapper.fromEntityToResponse(updated)
        ));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updatePartial(
        @PathVariable("id") UUID id,
        @RequestBody @Valid UpdateWorkerPartialParameters body
    ) {
        Worker current = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch worker with specified id"));

        if (body.email() != null) current.setEmail(body.email());
        if (body.fullName() != null) current.setFullName(body.fullName());
        if (body.password() != null) current.setPassword(body.password());
        if (body.role() != null) current.setRole(body.role());

        Pizzeria pizzeria = this.pizzeriaRepository.findByCode(body.pizzeriaCode())
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve pizzeria with specified code"));
        current.setPizzeria(pizzeria);

        Worker updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated worker",
                this.mapper.fromEntityToResponse(updated)
            ));
    }
}
