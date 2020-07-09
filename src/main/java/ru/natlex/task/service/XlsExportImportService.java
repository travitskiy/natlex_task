package ru.natlex.task.service;

import org.springframework.web.multipart.MultipartFile;
import ru.natlex.task.model.AsyncJob;

import java.io.IOException;

public interface XlsExportImportService {

    AsyncJob importSections(MultipartFile file) throws IOException;

    AsyncJob exportSections();

    void importSections(AsyncJob asyncJob, byte[] excelFileBytes);

    void exportSections(AsyncJob asyncJob);
}
