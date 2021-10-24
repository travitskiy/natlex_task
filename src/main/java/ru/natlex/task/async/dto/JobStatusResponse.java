package ru.natlex.task.async.dto;

import ru.natlex.task.async.job.AsyncJobStatus;

public class JobStatusResponse {
    private AsyncJobStatus status;

    public JobStatusResponse(AsyncJobStatus status) {
        this.status = status;
    }

    public AsyncJobStatus getStatus() {
        return status;
    }

    public void setStatus(AsyncJobStatus status) {
        this.status = status;
    }
}
