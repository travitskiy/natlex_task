package ru.natlex.task.async.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natlex.task.async.model.AsyncJob;
import ru.natlex.task.async.job.AsyncJobType;
import ru.natlex.task.xlsexport.model.SectionsExportFile;

import java.util.Optional;

public interface AsyncJobsRepository extends JpaRepository<AsyncJob, Long> {

    Optional<AsyncJob> findAsyncJobByIdAndJobType(Long id, AsyncJobType type);

}
