package pizzahub.api.presentation.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.user.worker.Worker;
import pizzahub.api.entities.user.worker.data.CreateWorkerParameters;
import pizzahub.api.entities.user.worker.data.UpdateWorkerPartialParameters;
import pizzahub.api.entities.user.worker.data.WorkerResponse;
import pizzahub.api.entities.user.worker.data.UpdateWorkerParameters;
import pizzahub.api.mappers.WorkerMapper;
import pizzahub.api.repositories.PizzeriaRepository;
import pizzahub.api.repositories.WorkerRepository;
import pizzahub.api.presentation.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    @Autowired
    WorkerRepository repository;

    @Autowired
    PizzeriaRepository pizzeriaRepository;

    @GetMapping
    public ResponseEntity<Response> fetchWorkers(
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
                paginated.stream()
                    .map(WorkerMapper::modelToResponse)
                    .collect(Collectors.toList())
            ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchWorkerById(@PathVariable("id") Long id) {
        Worker worker = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not find worker with specified id"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched worker with specified id",
                WorkerMapper.modelToResponse(worker)));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid CreateWorkerParameters body) {
        Worker worker = WorkerMapper.createParametersToModel(body);

        Worker created = this.repository.save(worker);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully created worker",
               WorkerMapper.modelToResponse(created)
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteWorker(@PathVariable("id") Long id) {
        Worker exists = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not find worker with specified id"));

        this.repository.deleteById(id);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("Successfully deleted worker with specified id", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(
        @PathVariable("id") Long id,
        @RequestBody @Valid UpdateWorkerParameters body
    ) {
        Worker current = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch worker with specified id"));

        if (body.email() != null && body.fullname() != null && body.password() != null && body.role() != null) {
            current.setEmail(body.email());
            current.setFullname(body.fullname());
            current.setPassword(body.password());
            current.setRole(body.role());
        }

        Worker updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated worker",
                WorkerMapper.modelToResponse(updated)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updatePartial(
        @PathVariable("id") Long id,
        @RequestBody @Valid UpdateWorkerPartialParameters body
    ) {
        Worker current = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch worker with specified id"));

        if (body.email() != null) current.setEmail(body.email());
        if (body.fullname() != null) current.setFullname(body.fullname());
        if (body.password() != null) current.setPassword(body.password());
        if (body.role() != null) current.setRole(body.role());

        Worker updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated worker",
                WorkerMapper.modelToResponse(updated)
            ));
    }
}
