package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pizzahub.api.entities.user.worker.Worker;

import java.util.UUID;

public interface WorkerRepository extends JpaRepository <Worker, UUID> {}
