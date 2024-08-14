package pizzahub.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pizzahub.api.entities.user.worker.Worker;
import pizzahub.api.entities.user.worker.data.SaveWorkerParameters;
import pizzahub.api.entities.user.worker.data.WorkerResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkerMapper {
    @Mapping(source = "pizzeria.code", target = "pizzeriaCode")
    WorkerResponse fromEntityToResponse(Worker entity);
    List<WorkerResponse> fromEntityListToResponseList(List<Worker> list);

    @Mapping(target = "pizzeria", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Worker fromSaveParametersToEntity(SaveWorkerParameters parameters);

    @Mapping(target = "pizzeria", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateWorker(@MappingTarget Worker target, SaveWorkerParameters parameters);
}
