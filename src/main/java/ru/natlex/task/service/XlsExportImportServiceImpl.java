package ru.natlex.task.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.natlex.task.exception.AsyncJobNotFoundException;
import ru.natlex.task.job.AsyncJobStatus;
import ru.natlex.task.job.AsyncJobType;
import ru.natlex.task.model.AsyncJob;
import ru.natlex.task.model.Geo;
import ru.natlex.task.model.Section;
import ru.natlex.task.model.SectionsExportFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class XlsExportImportServiceImpl implements XlsExportImportService {

    private final SectionService sectionService;
    private final AsyncJobService asyncJobService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public XlsExportImportServiceImpl(SectionService sectionService,
                                      AsyncJobService asyncJobService) {
        this.sectionService = sectionService;
        this.asyncJobService = asyncJobService;
    }

    @Override
    public AsyncJob importSections(MultipartFile file) throws IOException {
        final AsyncJob asyncJob = asyncJobService.getAsyncJob(AsyncJobType.IMPORT);
        final byte[] fileBytes = file.getBytes();
        if(fileBytes.length != 0)
            executorService.submit(() -> importSections(asyncJob, fileBytes));
        else
            return asyncJobService.updateStatusAsyncJob(asyncJob, AsyncJobStatus.ERROR)
                    .orElseThrow(()-> new AsyncJobNotFoundException(asyncJob.getId()));

        return asyncJob;
    }

    @Override
    public AsyncJob exportSections() {
        final AsyncJob asyncJob = asyncJobService.getAsyncJob(AsyncJobType.EXPORT);
        executorService.submit(() -> exportSections(asyncJob));
        return asyncJob;
    }

    public void importSections(AsyncJob asyncJob, byte[] excelFileBytes) {
        ByteArrayInputStream excelFile = new ByteArrayInputStream(excelFileBytes);
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
            asyncJobService.updateStatusAsyncJob(asyncJob, AsyncJobStatus.ERROR);
            return;
        }

        Iterator<Row> iterator = workbook.getSheetAt(0).iterator();
        //skip skipping the headers line
        if (iterator.hasNext())
            iterator.next();
        else
            try {
                throw new FileFormatException();
            } catch (FileFormatException e) {
                e.printStackTrace();
                return;
            }

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

                section.addGeo(new Geo(geoClassName, geoClassCode));
            }
            sectionService.saveSection(section);
        }
        asyncJobService.updateStatusAsyncJob(asyncJob, AsyncJobStatus.DONE);
    }

    public void exportSections(AsyncJob asyncJob) {
        List<Section> sections = sectionService.getAllSections();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sections");
        int maxGeoCount = 0;
        for (Section section : sections)
            if (section.getGeoList().size() > maxGeoCount)
                maxGeoCount = section.getGeoList().size();

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
            for (Geo geo : section.getGeoList()) {
                cell = row.createCell(cell.getColumnIndex() + 1);
                cell.setCellValue(geo.getName());
                cell = row.createCell(cell.getColumnIndex() + 1);
                cell.setCellValue(geo.getCode());
            }
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            workbook.close();

            sectionService.saveExportFile(new SectionsExportFile(asyncJob, byteArrayOutputStream.toByteArray()));
            asyncJobService.updateStatusAsyncJob(asyncJob, AsyncJobStatus.DONE);
        } catch (Exception e) {
            e.printStackTrace();
            asyncJobService.updateStatusAsyncJob(asyncJob, AsyncJobStatus.ERROR);
        }
    }
}

