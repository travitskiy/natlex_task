package ru.natlex.task.async.mapper;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.natlex.task.async.model.AsyncJob;
import ru.natlex.task.async.dto.JobStatusResponse;
import ru.natlex.task.async.dto.JobCreatedResponse;
import ru.natlex.task.xlsexport.model.SectionsExportFile;

@Component
public class JobMapper {

    public JobStatusResponse createJobStatusResponse(AsyncJob asyncJob) {
        return new JobStatusResponse(asyncJob.getStatus());
    }


    public JobCreatedResponse createJobCreatedResponse(AsyncJob asyncJob) {
        return new JobCreatedResponse(asyncJob.getId());
    }
}
