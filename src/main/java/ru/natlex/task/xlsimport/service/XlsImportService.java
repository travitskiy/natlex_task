package ru.natlex.task.xlsimport.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.natlex.task.async.service.AsyncJobService;
import ru.natlex.task.common.exception.AsyncJobNotFoundException;
import ru.natlex.task.async.job.AsyncJobStatus;
import ru.natlex.task.async.job.AsyncJobType;
import ru.natlex.task.async.model.AsyncJob;
import ru.natlex.task.geologicalglass.model.GeologicalClass;
import ru.natlex.task.sections.model.Section;
import ru.natlex.task.sections.service.SectionsService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class XlsImportService {

    @Lazy
    @Autowired
    private XlsImportService self;

    private final SectionsService sectionsService;
    private final AsyncJobService asyncJobService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public XlsImportService(SectionsService sectionsService,
                            AsyncJobService asyncJobService) {
        this.sectionsService = sectionsService;
        this.asyncJobService = asyncJobService;
    }

    public AsyncJob getJobStatus(Long asyncJobId) {
        return asyncJobService.findAsyncJob(asyncJobId, AsyncJobType.IMPORT)
                .orElseThrow(() -> new AsyncJobNotFoundException(asyncJobId));
    }

    public AsyncJob startImportFileJob(MultipartFile file) throws IOException {
        final AsyncJob asyncJob = asyncJobService.getAsyncJob(AsyncJobType.IMPORT);
        final byte[] fileBytes = file.getBytes();
        if (fileBytes.length != 0)
            executorService.submit(() -> self.importSections(asyncJob, fileBytes));
        else
            return asyncJobService.updateStatusAsyncJob(asyncJob, AsyncJobStatus.ERROR)
                    .orElseThrow(() -> new AsyncJobNotFoundException(asyncJob.getId()));

        return asyncJob;
    }

    @Transactional
    public void importSections(AsyncJob asyncJob, byte[] excelFileBytes) {
        try {
            ByteArrayInputStream excelFile = new ByteArrayInputStream(excelFileBytes);
            Workbook workbook = new XSSFWorkbook(excelFile);

            Iterator<Row> iterator = workbook.getSheetAt(0).iterator();
            //skip skipping the headers line
            if (iterator.hasNext())
                iterator.next();
            else
                throw new FileFormatException();

            List<Section> sections = new ArrayList<>();
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                //get Section
                String sectionName;
                if (cellIterator.hasNext())
                    sectionName = cellIterator.next().getStringCellValue();
                else
                    break;

                Section section = new Section(sectionName);

                //get geo classes
                while (cellIterator.hasNext()) {
                    String geoClassName = cellIterator.next().getStringCellValue();
                    String geoClassCode;
                    //if has classCode
                    if (cellIterator.hasNext())
                        geoClassCode = cellIterator.next().getStringCellValue();
                    else
                        break;

                    //check is empty strings
                    if (geoClassName.isEmpty() || geoClassCode.isEmpty())
                        continue;

                    GeologicalClass geologicalClass = new GeologicalClass(geoClassName, geoClassCode);
                    geologicalClass.setSection(section);
                    section.getGeoList().add(geologicalClass);
                }
                sections.add(section);
            }
            sectionsService.saveAllSections(sections);
            asyncJobService.updateStatusAsyncJob(asyncJob, AsyncJobStatus.DONE);
        } catch (Exception e) {
            e.printStackTrace();
            asyncJobService.updateStatusAsyncJob(asyncJob, AsyncJobStatus.ERROR);
        }
    }
}

