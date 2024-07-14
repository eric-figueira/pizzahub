package pizzahub.api.mappers;

import pizzahub.api.entities.user.worker.Worker;
import pizzahub.api.entities.user.worker.data.CreateWorkerParameters;
import pizzahub.api.entities.user.worker.data.WorkerResponse;

public class WorkerMapper {
    public static WorkerResponse modelToResponse(Worker model) {
        return new WorkerResponse(
            model.getId(),
            model.getFullname(),
            model.getEmail(),
            model.getRole(),
            model.getPizzeria().getCode()
        );
    }

    public static Worker createParametersToModel(CreateWorkerParameters parameters) {
        return new Worker(
            parameters.fullname(),
            parameters.email(),
            parameters.password(),
            parameters.role()
        );
    }
}
