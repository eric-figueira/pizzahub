package pizzahub.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pizzahub.api.entities.user.worker.Worker;

public interface WorkerRepository extends JpaRepository <Worker, Long> {}
