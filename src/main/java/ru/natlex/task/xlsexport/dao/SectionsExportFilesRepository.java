package ru.natlex.task.xlsexport.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natlex.task.async.model.AsyncJob;
import ru.natlex.task.xlsexport.model.SectionsExportFile;

import java.util.Optional;

public interface SectionsExportFilesRepository extends JpaRepository<SectionsExportFile, Long> {
    Optional<SectionsExportFile> findByAsyncJob(AsyncJob asyncJob);
}
