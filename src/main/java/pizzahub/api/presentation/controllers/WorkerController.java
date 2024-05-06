package pizzahub.api.presentation.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pizzahub.api.entities.user.worker.Worker;
import pizzahub.api.entities.user.worker.data.CreateWorkerRequestDTO;
import pizzahub.api.repositories.WorkerRepository;
import pizzahub.api.presentation.Response;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    @Autowired
    WorkerRepository repository;

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
        List<CreateWorkerRequestDTO> ret = new ArrayList<CreateWorkerRequestDTO>();
        paginated.forEach(a -> ret.add(a.createWorkerRequestDTO()));

        return ResponseEntity
            .ok()
            .body(new Response(
                "Successfully fetched all workers",
                ret
                ));
    }
}
