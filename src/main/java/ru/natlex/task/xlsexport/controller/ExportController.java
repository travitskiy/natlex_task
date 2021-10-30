package ru.natlex.task.xlsexport.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.natlex.task.async.dto.JobStatusResponse;
import ru.natlex.task.async.dto.JobCreatedResponse;
import ru.natlex.task.xlsexport.service.XlsExportRestService;

@RestController
public class ExportController {

    private final XlsExportRestService xlsExportRestService;

    public ExportController(XlsExportRestService xlsExportRestService) {
        this.xlsExportRestService = xlsExportRestService;
    }

    //API GET /export/{id} returns result of parsed file by Job ID ("DONE", "IN PROGRESS", "ERROR")
    @GetMapping(value = "/export/{asyncJobId}", produces = "application/json")
    JobStatusResponse getExportStatus(@PathVariable Long asyncJobId) {
        return xlsExportRestService.getJobStatus(asyncJobId);
    }

    //API GET /export returns ID of the Async Job and launches exporting
    @GetMapping(value = "/export", produces = "application/json")
    JobCreatedResponse createExportFile() {
        return xlsExportRestService.createExportFile();
    }

    //API GET /export/{id}/file returns a file by Job ID (throw an exception if exporting is in process)
    @GetMapping(value = "/export/{asyncJobId}/file", produces = "application/json")
    @ResponseBody
    ResponseEntity<Resource> getExportFile(@PathVariable Long asyncJobId) {
        return xlsExportRestService.getExportFileByJob(asyncJobId);
    }
}
