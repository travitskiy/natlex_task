package ru.natlex.task.xlsexport.mapper;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.natlex.task.xlsexport.model.SectionsExportFile;

@Component
public class ExportMapper {
    public ResponseEntity<Resource> createExportFileResponse(SectionsExportFile sectionsExportFile) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(sectionsExportFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sectionsExportFile.getFileName() + "\"")
                .body(new ByteArrayResource(sectionsExportFile.getData()));
    }
}
