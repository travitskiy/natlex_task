package ru.natlex.task.async.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.natlex.task.async.job.AsyncJobStatus;
import ru.natlex.task.async.job.AsyncJobType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "async_job")
public class AsyncJob {
    @Id
    @GeneratedValue(generator = "async_job_id_generator")
    private Long id;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private AsyncJobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_status")
    private AsyncJobStatus status;

    public AsyncJob() { }

    public AsyncJob(AsyncJobType jobType) {
        this.jobType = jobType;
        this.status = AsyncJobStatus.IN_PROGRESS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AsyncJobType getJobType() {
        return jobType;
    }

    public void setJobType(AsyncJobType jobType) {
        this.jobType = jobType;
    }

    public AsyncJobStatus getStatus() {
        return status;
    }

    public void setStatus(AsyncJobStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsyncJob asyncJob = (AsyncJob) o;
        return Objects.equals(id, asyncJob.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
