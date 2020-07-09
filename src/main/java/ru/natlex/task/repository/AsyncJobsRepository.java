package ru.natlex.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natlex.task.model.AsyncJob;
import ru.natlex.task.job.AsyncJobType;

import java.util.Optional;

public interface AsyncJobsRepository extends JpaRepository<AsyncJob, Long> {

    Optional<AsyncJob> findAsyncJobByIdAndJobType(Long id, AsyncJobType type);
}
