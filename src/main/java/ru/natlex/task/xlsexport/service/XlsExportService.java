package ru.natlex.task.xlsexport.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natlex.task.async.job.AsyncJobStatus;
import ru.natlex.task.async.job.AsyncJobType;
import ru.natlex.task.async.model.AsyncJob;
import ru.natlex.task.async.service.AsyncJobService;
import ru.natlex.task.common.exception.AsyncJobNotFoundException;
import ru.natlex.task.common.exception.ExportFileNotFoundException;
import ru.natlex.task.common.exception.ExportingInProcessException;
import ru.natlex.task.geologicalglass.model.GeologicalClass;
import ru.natlex.task.sections.model.Section;
import ru.natlex.task.sections.service.SectionsService;
import ru.natlex.task.xlsexport.model.SectionsExportFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class XlsExportService {
    @Lazy
    @Autowired
    private XlsExportService self;
    private final SectionsService sectionsService;
    private final AsyncJobService asyncJobService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public XlsExportService(SectionsService sectionsService,
                            AsyncJobService asyncJobService) {
        this.sectionsService = sectionsService;
        this.asyncJobService = asyncJobService;
    }

    public AsyncJob startExportFileJob() {
        final AsyncJob asyncJob = asyncJobService.getAsyncJob(AsyncJobType.EXPORT);
        executorService.submit(() -> self.exportSections(asyncJob));
        return asyncJob;
    }

    public SectionsExportFile getExportFileByJob(Long asyncJobId) {
        AsyncJob asyncJob = this.getJobStatus(asyncJobId);

        if (asyncJob.getStatus() == AsyncJobStatus.IN_PROGRESS)
            throw new ExportingInProcessException(asyncJobId);

        return sectionsService.findExportedFile(asyncJob)
                .orElseThrow(() -> new ExportFileNotFoundException(asyncJobId));
    }

    public AsyncJob getJobStatus(Long asyncJobId) {
        return asyncJobService.findAsyncJob(asyncJobId, AsyncJobType.EXPORT)
                .orElseThrow(() -> new AsyncJobNotFoundException(asyncJobId));
    }

    @Transactional
    public void exportSections(AsyncJob asyncJob) {
        try {
            List<Section> sections = sectionsService.getAllSections();

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Sections");
            int maxGeoCount = 0;
            for (Section section : sections) {
                if (section.getGeoList().size() > maxGeoCount)
                    maxGeoCount = section.getGeoList().size();
            }

            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            //write header
            cell.setCellValue("Section name");

            for (int i = 0; i < maxGeoCount; i++) {
                cell = row.createCell(cell.getColumnIndex() + 1);
                cell.setCellValue("Class " + i + " name");
                cell = row.createCell(cell.getColumnIndex() + 1);
                cell.setCellValue("Class " + i + " code");
            }
            //write objects
            for (Section section : sections) {
                row = sheet.createRow(row.getRowNum() + 1);
                cell = row.createCell(0);
                cell.setCellValue(section.getName());
                for (GeologicalClass geologicalClass : section.getGeoList()) {
                    cell = row.createCell(cell.getColumnIndex() + 1);
                    cell.setCellValue(geologicalClass.getName());
                    cell = row.createCell(cell.getColumnIndex() + 1);
                    cell.setCellValue(geologicalClass.getCode());
                }
            }

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                workbook.write(byteArrayOutputStream);
                workbook.close();

                sectionsService.saveExportFile(new SectionsExportFile(asyncJob, byteArrayOutputStream.toByteArray()));
                asyncJobService.updateStatusAsyncJob(asyncJob, AsyncJobStatus.DONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            asyncJobService.updateStatusAsyncJob(asyncJob, AsyncJobStatus.ERROR);
        }
    }
}
