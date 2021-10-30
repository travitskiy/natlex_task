package ru.natlex.task.xlsimport.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.natlex.task.async.dto.JobCreatedResponse;
import ru.natlex.task.async.dto.JobStatusResponse;
import ru.natlex.task.xlsimport.service.XlsImportRestService;

import java.io.IOException;

@RestController
public class ImportController {
    private final XlsImportRestService xlsImportRestService;

    public ImportController(XlsImportRestService xlsImportRestService) {
        this.xlsImportRestService = xlsImportRestService;
    }

    //API GET /import/{id} returns result of importing by Job ID ("DONE", "IN PROGRESS", "ERROR")
    @GetMapping(value = "/import/{id}", produces = "application/json")
    JobStatusResponse getImportStatus(@PathVariable Long id) {
        return xlsImportRestService.getJobStatus(id);
    }

    //API POST /import (file) returns ID of the Async Job and launches importing
    @PostMapping(value = "/import", produces = "application/json")
    JobCreatedResponse importFile(@RequestParam("file") MultipartFile file) throws IOException {
        return xlsImportRestService.createImportFile(file);
    }
}
