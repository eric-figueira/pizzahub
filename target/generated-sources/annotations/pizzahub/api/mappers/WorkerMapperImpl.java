package pizzahub.api.mappers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.user.Role;
import pizzahub.api.entities.user.worker.Worker;
import pizzahub.api.entities.user.worker.data.SaveWorkerParameters;
import pizzahub.api.entities.user.worker.data.WorkerResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-15T09:43:02-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Azul Systems, Inc.)"
)
@Component
public class WorkerMapperImpl implements WorkerMapper {

    @Override
    public WorkerResponse fromEntityToResponse(Worker entity) {
        if ( entity == null ) {
            return null;
        }

        Short pizzeriaCode = null;
        UUID id = null;
        String fullName = null;
        String email = null;
        Role role = null;
        Date createdAt = null;

        pizzeriaCode = entityPizzeriaCode( entity );
        id = entity.getId();
        fullName = entity.getFullName();
        email = entity.getEmail();
        role = entity.getRole();
        createdAt = entity.getCreatedAt();

        WorkerResponse workerResponse = new WorkerResponse( id, fullName, email, role, createdAt, pizzeriaCode );

        return workerResponse;
    }

    @Override
    public List<WorkerResponse> fromEntityListToResponseList(List<Worker> list) {
        if ( list == null ) {
            return null;
        }

        List<WorkerResponse> list1 = new ArrayList<WorkerResponse>( list.size() );
        for ( Worker worker : list ) {
            list1.add( fromEntityToResponse( worker ) );
        }

        return list1;
    }

    @Override
    public Worker fromSaveParametersToEntity(SaveWorkerParameters parameters) {
        if ( parameters == null ) {
            return null;
        }

        Worker worker = new Worker();

        worker.setFullName( parameters.fullName() );
        worker.setEmail( parameters.email() );
        worker.setPassword( parameters.password() );
        worker.setRole( parameters.role() );

        return worker;
    }

    @Override
    public void updateWorker(Worker target, SaveWorkerParameters parameters) {
        if ( parameters == null ) {
            return;
        }

        target.setFullName( parameters.fullName() );
        target.setEmail( parameters.email() );
        target.setPassword( parameters.password() );
        target.setRole( parameters.role() );
    }

    private Short entityPizzeriaCode(Worker worker) {
        if ( worker == null ) {
            return null;
        }
        Pizzeria pizzeria = worker.getPizzeria();
        if ( pizzeria == null ) {
            return null;
        }
        Short code = pizzeria.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }
}
