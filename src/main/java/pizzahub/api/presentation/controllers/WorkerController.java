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

    @PutMapping
    public ResponseEntity<Response> updateWorker(@RequestBody @Valid UpdateWorkerParameters body) {
        Optional<Worker> workerOptional = this.repository.findById(body.id());

        if (workerOptional.isPresent()) {
            Worker worker = workerOptional.get();

            try {
                if (body.fullname().length() != 0 || body.fullname() != null) {
                    try {
                        worker.setFullname(body.fullname());
                    } catch (Exception error) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid fullname format", null));
                    }
                }

                if (body.email().length() != 0 || body.email() != null) {
                    try {
                        worker.setEmail(body.email());
                    } catch (Exception error) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid email format", null));
                    }
                }

                if (body.password().length() != 0 || body.password() != null) {
                    try {
                        worker.setPassword(body.password());
                    } catch (Exception error) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid password format", null));
                    }
                }

                if (body.role() != null) {
                    try {
                        worker.setRole(body.role());
                    } catch (Exception error) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid role", null));
                    }
                }

                if (body.pizzeriaCode() == 0 || body.pizzeriaCode() == null) {
                    try {
                        Optional<Pizzeria> pizzeria = pizzeriaRepository.findByCode(body.pizzeriaCode());

                        if (pizzeria.isPresent()) {
                            worker.setPizzeria(pizzeria.get());
                        } else {
                            return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new Response("Could not find pizzeria with specified id", null));
                        }

                    } catch (Exception error) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Invalid pizzaria code format", null));
                    }
                }

                Worker updatedWorker = this.repository.save(worker);

                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Response(
                        "Successfully updated worker",
                        updatedWorker.convertToResponseDTO()
                ));
            }
            catch (Exception error) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Response(
                        "Failed to retrieve informed parameters",
                        null
                    ));
            }
        }
        else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response(
                    "Failed to update worker",
                    null
                ));
        }
    }
}
