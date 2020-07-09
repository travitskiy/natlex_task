package ru.natlex.task.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.natlex.task.exception.AsyncJobNotFoundException;
import ru.natlex.task.exception.ExportFileNotFoundException;
import ru.natlex.task.exception.ExportingInProcessException;
import ru.natlex.task.job.AsyncJobStatus;
import ru.natlex.task.job.AsyncJobType;
import ru.natlex.task.model.AsyncJob;
import ru.natlex.task.model.Section;
import ru.natlex.task.model.SectionsExportFile;
import ru.natlex.task.service.AsyncJobService;
import ru.natlex.task.service.SectionService;
import ru.natlex.task.service.XlsExportImportService;

import java.io.IOException;
import java.util.List;

@RestController
public class SectionController {
    private final SectionService sectionService;
    private final AsyncJobService asyncJobService;
    private final XlsExportImportService xlsExportImportService;

    SectionController(SectionService sectionService, AsyncJobService asyncJobService, XlsExportImportService xlsExportImportService) {
        this.sectionService = sectionService;
        this.asyncJobService = asyncJobService;
        this.xlsExportImportService = xlsExportImportService;
    }

    //returns a list of all Sections that have geologicalClasses
    //with the specified code.
    @GetMapping(value = "/sections/by-code", produces = "application/json")
    List<Section> getSectionsConsistsCode(@RequestParam String code) {
        return sectionService.getSectionsConsistCode(code);
    }

    //API GET /import/{id} returns result of importing by Job ID ("DONE", "IN PROGRESS", "ERROR")
    @GetMapping(value = "/import/{id}", produces = "application/json")
    AsyncJob getImportStatus(@PathVariable Long id) {
        return asyncJobService.findAsyncJob(id, AsyncJobType.IMPORT).orElseThrow(() -> new AsyncJobNotFoundException(id));
    }

    //API GET /export/{id} returns result of parsed file by Job ID ("DONE", "IN PROGRESS", "ERROR")
    @GetMapping(value = "/export/{id}", produces = "application/json")
    AsyncJob getExportStatus(@PathVariable Long id) {
        return asyncJobService.findAsyncJob(id, AsyncJobType.EXPORT).orElseThrow(() -> new AsyncJobNotFoundException(id));
    }

    //API POST /import (file) returns ID of the Async Job and launches importing
    @PostMapping(value = "/import", produces = "application/json")
    AsyncJob importFile(@RequestParam("file") MultipartFile file) throws IOException {
        return xlsExportImportService.importSections(file);
    }

    //API GET /export returns ID of the Async Job and launches exporting
    @GetMapping(value = "/export", produces = "application/json")
    AsyncJob exportFile() {
        return xlsExportImportService.exportSections();
    }

    //API GET /export/{id}/file returns a file by Job ID (throw an exception if exporting is in process)
    @GetMapping(value = "/export/{id}/file", produces = "application/json")
    @ResponseBody
    ResponseEntity<Resource> getExportFile(@PathVariable Long id) {
        AsyncJob asyncJob = asyncJobService.findAsyncJob(id, AsyncJobType.EXPORT)
                .orElseThrow(() -> new AsyncJobNotFoundException(id));

        if (asyncJob.getStatus() == AsyncJobStatus.IN_PROGRESS)
            throw new ExportingInProcessException(id);

        SectionsExportFile exportFile = sectionService.findExportedFile(asyncJob)
                .orElseThrow(() -> new ExportFileNotFoundException(id));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(exportFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + exportFile.getFileName() + "\"")
                .body(new ByteArrayResource(exportFile.getData()));
    }
}
