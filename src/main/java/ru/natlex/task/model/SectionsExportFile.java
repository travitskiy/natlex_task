package ru.natlex.task.model;

import javax.persistence.*;

@Entity
@Table(name = "exported_files")
public class SectionsExportFile {
    private @Id
    @GeneratedValue(generator = "exported_files_id_generator")
    Long id;

    @Lob
    private byte[] data;
    @Column(name = "file_type")
    private String fileType = "application/vnd.ms-excel";
    @Column(name = "file_name")
    private String fileName;
    @OneToOne(cascade=CascadeType.DETACH)
    @JoinColumn(name = "async_job_id")
    private AsyncJob asyncJob;

    public SectionsExportFile() { }

    public SectionsExportFile(AsyncJob asyncJob, byte[] data) {
        this.asyncJob = asyncJob;
        this.data = data;
        this.fileName = "sections.xlsx";
    }

    public AsyncJob getAsyncJob() {
        return asyncJob;
    }

    public void setAsyncJob(AsyncJob asyncJob) {
        this.asyncJob = asyncJob;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
