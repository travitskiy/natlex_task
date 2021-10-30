package ru.natlex.task.sections.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natlex.task.async.model.AsyncJob;
import ru.natlex.task.xlsexport.dao.SectionsExportFilesRepository;
import ru.natlex.task.xlsexport.model.SectionsExportFile;
import ru.natlex.task.geologicalglass.model.GeologicalClass;
import ru.natlex.task.sections.model.Section;
import ru.natlex.task.sections.dao.SectionsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SectionsService {

    private final SectionsRepository sectionsRepository;
    private final SectionsExportFilesRepository sectionsExportFilesRepository;

    public SectionsService(SectionsRepository repository,
                           SectionsExportFilesRepository sectionsExportFilesRepository) {
        this.sectionsRepository = repository;
        this.sectionsExportFilesRepository = sectionsExportFilesRepository;
    }

    @Transactional
    public Optional<Section> saveSection(Section section) {
        return Optional.of(sectionsRepository.saveAndFlush(section));
    }

    @Transactional
    public List<Section> saveAllSections(List<Section> section) {
        return sectionsRepository.saveAll(section);
    }

    @Transactional(readOnly = true)
    public List<Section> getAllSections() {
        return sectionsRepository.findAll();
    }

    @Transactional
    public SectionsExportFile saveExportFile(SectionsExportFile sectionsExportFile) {
        return sectionsExportFilesRepository.save(sectionsExportFile);
    }

    @Transactional(readOnly = true)
    public Optional<SectionsExportFile> findExportedFile(AsyncJob asyncJob) {
        return sectionsExportFilesRepository.findByAsyncJob(asyncJob);
    }

    @Transactional(readOnly = true)
    public List<Section> getSectionsByGeoCode(String code) {
        return sectionsRepository.getSectionsByGeoCode(code);
    }

    @Transactional(readOnly = true)
    public Section getSection(Long id) {
        return sectionsRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public void deleteSection(Section section) {
        sectionsRepository.findById(section.getId()).ifPresent(fs -> {
            sectionsRepository.deleteById(fs.getId());
        });
    }

    @Transactional
    public Optional<SectionsExportFile> updateSection(Long id, Section section) throws Exception {
        if (getSection(id) == null)
            throw new Exception("Section not found");
        return saveSection(section).orElseThrow();
    }
}
