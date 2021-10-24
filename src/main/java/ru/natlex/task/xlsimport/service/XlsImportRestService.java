package ru.natlex.task.xlsimport.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.natlex.task.async.dto.JobCreatedResponse;
import ru.natlex.task.async.dto.JobStatusResponse;
import ru.natlex.task.async.mapper.JobMapper;

import java.io.IOException;

@Service
public class XlsImportRestService {
    private final XlsImportService xlsImportService;
    private final JobMapper jobMapper;

    public XlsImportRestService(XlsImportService xlsImportService,
                                JobMapper jobMapper) {
        this.xlsImportService = xlsImportService;
        this.jobMapper = jobMapper;
    }

    public JobCreatedResponse createImportFile(MultipartFile file) throws IOException {
        return jobMapper.createJobCreatedResponse(xlsImportService.startImportFileJob(file));
    }

    public JobStatusResponse getJobStatus(Long jobId) {
        return jobMapper.createJobStatusResponse(xlsImportService.getJobStatus(jobId));
    }
}
