package ru.natlex.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natlex.task.model.AsyncJob;
import ru.natlex.task.model.SectionsExportFile;

import javax.websocket.RemoteEndpoint;
import java.util.Optional;

public interface SectionsExportFilesRepository extends JpaRepository<SectionsExportFile, Long> {
    Optional<SectionsExportFile> findByAsyncJob(AsyncJob asyncJob);
}
