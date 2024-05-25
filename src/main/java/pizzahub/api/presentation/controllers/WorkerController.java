package pizzahub.api.presentation.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import pizzahub.api.entities.user.worker.data.CreateWorkerRequestDTO;
import pizzahub.api.entities.user.worker.data.WorkerResponseDTO;
import pizzahub.api.entities.user.worker.data.UpdateWorkerRequestDTO;
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
        @RequestParam(value = "perPage", defaultValue = "30") short perPage,
        @RequestParam(value = "orderBy", defaultValue = "name") String name
    ) {
        List<Worker> all = this.repository.findAll();

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

        List<Worker> paginated = all.subList(start, end);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched all workers",
                paginated.stream()
                    .map(worker -> worker.convertToResponseDTO())
                    .collect(Collectors.toList())
            ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchWorkerById(@PathVariable("id") Long workerId) {
        Optional<Worker> workerOptional = this.repository.findById(workerId);

        if (workerOptional.isPresent()) {
            Worker worker = workerOptional.get();
            WorkerResponseDTO response = worker.convertToResponseDTO();

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully fetched worker with specified id", response));
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response("Could not find worker with specified id", null));
        }
    }

    @PostMapping
    public ResponseEntity<Response> createWorker(@RequestBody @Valid CreateWorkerRequestDTO body) {
        try {
            Worker worker = new Worker(body);
            try {
                Optional<Pizzeria> pizzeria = pizzeriaRepository.findByCode(body.pizzeriaCode());

                if (pizzeria.isPresent()) {
                    worker.setPizzeria(pizzeria.get());
                } else {
                    return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new Response("Could not find pizzeria with specified id", null));
                }

                Worker createdWorker = this.repository.save(worker);

                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Response(
                        "Successfully created worker",
                        createdWorker.convertToResponseDTO()
                    ));
            }
            catch (Exception error) {
                return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("Could not create new worker", null));
            }
        }
        catch (Exception error) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response("Could not create new worker", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteWorker(@PathVariable(name = "id") Long workerId) {
        Optional<Worker> workerOptional = this.repository.findById(workerId);

        if (workerOptional.isPresent()) {
            this.repository.deleteById(workerId);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully deleted worker with specified id", null));
        }
        else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response("Worker with specified id does not exist", null));
        }
    }

    @PutMapping
    public ResponseEntity<Response> updateWorker(@RequestBody @Valid UpdateWorkerRequestDTO body) {
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
