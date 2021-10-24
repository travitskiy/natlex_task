package ru.natlex.task.xlsexport.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.natlex.task.async.dto.JobStatusResponse;
import ru.natlex.task.async.dto.JobCreatedResponse;
import ru.natlex.task.async.mapper.JobMapper;
import ru.natlex.task.xlsexport.mapper.ExportMapper;

@Service
public class XlsExportRestService {
    private final XlsExportService xlsExportService;
    private final JobMapper jobMapper;
    private final ExportMapper exportMapper;

    public XlsExportRestService(XlsExportService xlsExportService,
                                JobMapper jobMapper, ExportMapper exportMapper) {
        this.xlsExportService = xlsExportService;
        this.jobMapper = jobMapper;
        this.exportMapper = exportMapper;
    }

    public JobCreatedResponse createExportFile() {
        return jobMapper.createJobCreatedResponse(xlsExportService.startExportFileJob());
    }

    public JobStatusResponse getJobStatus(Long jobId) {
        return jobMapper.createJobStatusResponse(xlsExportService.getJobStatus(jobId));
    }

    public ResponseEntity<Resource> getExportFileByJob(Long jobId) {
        return exportMapper.createExportFileResponse(xlsExportService.getExportFileByJob(jobId));
    }
}

