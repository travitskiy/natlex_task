package ru.natlex.task.async.dto;

public class JobCreatedResponse {
    private Long jobId;

    public JobCreatedResponse(Long jobId) {
        this.jobId = jobId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
