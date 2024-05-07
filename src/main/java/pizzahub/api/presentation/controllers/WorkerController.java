package pizzahub.api.presentation.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.user.worker.Worker;
import pizzahub.api.entities.user.worker.data.CreateWorkerRequestDTO;
import pizzahub.api.entities.user.worker.data.FetchWorkerResponseDTO;
import pizzahub.api.repositories.PizzeriaRepository;
import pizzahub.api.repositories.WorkerRepository;
import pizzahub.api.presentation.Response;
import org.springframework.web.bind.annotation.PostMapping;
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
        @RequestParam(value="page", defaultValue="1") short page,
        @RequestParam(value="perPage", defaultValue="30") short perPage,
        @RequestParam(value="orderBy", defaultValue="name") String name
    ) {
        System.out.println("----- PAGE " + page);
        System.out.println("----- PER PAGE " + perPage);

        List<Worker> all = this.repository.findAll();

        short start = (short)((page - 1)*(perPage));
        short end = (short)(page*perPage);

        if (start < 0 || end >= all.size()) {
            return ResponseEntity
                .badRequest()
                .body(new Response("Invalid pagination parameters", null));
        }

        List<Worker> paginated = all.subList(start, end);
        List<FetchWorkerResponseDTO> ret = new ArrayList<FetchWorkerResponseDTO>();
        paginated.forEach(a -> ret.add(a.fetchWorkerResponseDTO()));

        return ResponseEntity
            .ok()
            .body(new Response(
                "Successfully fetched all workers",
                ret
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchWorkerById(@PathVariable("id") Long workerId) {
        Optional<Worker> workerOptional = this.repository.findById(workerId);

        if (workerOptional.isPresent()) {
            Worker worker = workerOptional.get();
            FetchWorkerResponseDTO response = worker.fetchWorkerResponseDTO();

            return ResponseEntity
                .ok()
                .body(new Response("Successfully fetched worker with specified id", response));
        }
        else {
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
                Short id = body.pizzeria_id();
                Optional<Pizzeria> pizzeria = pizzeriaRepository.findById(id);
                if (!pizzeria.isPresent())
            }
            catch (Exception error) {

            }
        }
        catch (Exception error) {
            ResponseEntity
                .internalServerError()
                .body(new Response("Could not create new worker", null));
        }
    }

}
