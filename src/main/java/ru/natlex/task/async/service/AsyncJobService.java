package ru.natlex.task.async.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.natlex.task.async.job.*;
import ru.natlex.task.async.model.AsyncJob;
import ru.natlex.task.async.dao.AsyncJobsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AsyncJobService {
    private final AsyncJobsRepository asyncJobsRepository;

    public AsyncJobService(AsyncJobsRepository asyncJobsRepository) {
        this.asyncJobsRepository = asyncJobsRepository;
    }

    private AsyncJob saveAsyncJob(AsyncJob asyncJob) {
        return asyncJobsRepository.save(asyncJob);
    }

    @Transactional(readOnly = true)
    public List<AsyncJob> getAllAsyncJobs() {
        return asyncJobsRepository.findAll();
    }

    @Transactional
    public AsyncJob getAsyncJob(AsyncJobType type) {
        return saveAsyncJob(new AsyncJob(type));
    }

    @Transactional(readOnly = true)
    public Optional<AsyncJob> findAsyncJob(Long id) {
        return asyncJobsRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<AsyncJob> findAsyncJob(Long id, AsyncJobType type) {
        return asyncJobsRepository.findAsyncJobByIdAndJobType(id, type);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<AsyncJob> updateStatusAsyncJob(AsyncJob asyncJob, AsyncJobStatus status) {
        if (findAsyncJob(asyncJob.getId()).isPresent()) {
            asyncJob.setStatus(status);
            return Optional.of(saveAsyncJob(asyncJob));
        } else
            return Optional.empty();
    }
}
