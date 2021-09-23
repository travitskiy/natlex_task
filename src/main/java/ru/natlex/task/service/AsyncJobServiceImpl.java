package ru.natlex.task.service;

import org.springframework.stereotype.Service;
import ru.natlex.task.job.*;
import ru.natlex.task.model.AsyncJob;
import ru.natlex.task.repository.AsyncJobsRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AsyncJobServiceImpl implements AsyncJobService {
    private final AsyncJobsRepository asyncJobsRepository;

    public AsyncJobServiceImpl(AsyncJobsRepository asyncJobsRepository) {
        this.asyncJobsRepository = asyncJobsRepository;
    }

    private AsyncJob saveAsyncJob(AsyncJob asyncJob) {
        return asyncJobsRepository.save(asyncJob);
    }

    @Override
    public List<AsyncJob> getAllAsyncJobs() {
        return asyncJobsRepository.findAll();
    }

    @Override
    public AsyncJob getAsyncJob(AsyncJobType type) {
        return saveAsyncJob(new AsyncJob(type));
    }

    @Override
    public Optional<AsyncJob> findAsyncJob(Long id) {
        return asyncJobsRepository
                .findById(id);
    }

    @Override
    public Optional<AsyncJob> findAsyncJob(Long id, AsyncJobType type) {
        return asyncJobsRepository
                .findAsyncJobByIdAndJobType(id, type);
    }

    @Override
    public Optional<AsyncJob> updateStatusAsyncJob(AsyncJob asyncJob, AsyncJobStatus status) {
        if (findAsyncJob(asyncJob.getId()).isPresent()) {
            asyncJob.setStatus(status);
            return Optional.of(saveAsyncJob(asyncJob));
        } else
            return Optional.empty();
    }
}
