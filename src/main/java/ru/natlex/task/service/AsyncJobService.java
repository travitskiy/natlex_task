package ru.natlex.task.service;

import ru.natlex.task.model.AsyncJob;
import ru.natlex.task.job.AsyncJobStatus;
import ru.natlex.task.job.AsyncJobType;

import java.util.List;
import java.util.Optional;

public interface AsyncJobService {
    List<AsyncJob> getAllAsyncJobs();

    Optional<AsyncJob> findAsyncJob(Long id, AsyncJobType type);

    Optional<AsyncJob> findAsyncJob(Long id);

    AsyncJob getAsyncJob(AsyncJobType type);

    Optional<AsyncJob> updateStatusAsyncJob(AsyncJob asyncJob, AsyncJobStatus status);
}
