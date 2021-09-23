package ru.natlex.task.service;

import org.springframework.stereotype.Service;
import ru.natlex.task.async.LockManager;
import ru.natlex.task.model.AsyncJob;
import ru.natlex.task.model.Geo;
import ru.natlex.task.model.Section;
import ru.natlex.task.model.SectionsExportFile;
import ru.natlex.task.repository.SectionsExportFilesRepository;
import ru.natlex.task.repository.SectionsRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class SectionServiceImpl implements SectionService {
    private final SectionsRepository repository;
    private final SectionsExportFilesRepository sectionsExportFilesRepository;
    private final LockManager lockManager;

    public SectionServiceImpl(SectionsRepository repository,
                              LockManager lockManager,
                              SectionsExportFilesRepository sectionsExportFilesRepository) {
        this.repository = repository;
        this.lockManager = lockManager;
        this.sectionsExportFilesRepository = sectionsExportFilesRepository;
    }

    @Override
    public SectionsExportFile saveExportFile(SectionsExportFile sectionsExportFile) {
        return sectionsExportFilesRepository.save(sectionsExportFile);
    }

    @Override
    public Optional<Section> saveSection(Section section) {
        ReentrantLock lock = lockManager.getLock(Section.class.getName() + section.hashCode());
        Section savedSection = null;
        lock.lock();

        if (!repository.findSectionByHash(section.hashCode()).isPresent())
            savedSection = repository.save(section);

        lock.unlock();

        return Optional.ofNullable(savedSection);
    }

    @Override
    public Optional<SectionsExportFile> findExportedFile(AsyncJob asyncJob) {
        return sectionsExportFilesRepository.findByAsyncJob(asyncJob);
    }

    @Override
    public List<Section> getSectionsConsistCode(String code) {
        return repository.findAll().stream()
                .flatMap(section -> section.getGeoList().stream())
                .filter(geo -> geo.getCode().equals(code)).map(Geo::getSection)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Section> getAllSections() {
        return repository.findAll();
    }

    @Override
    public Section getSection(Long id) {
        return repository.findById(id)
                .orElse(null);
    }

    @Override
    public void deleteSection(Section section) {
        repository.findById(section.getId()).ifPresent(fs -> {
            repository.deleteById(fs.getId());
        });
    }

    @Override
    public Optional<Section> updateSection(Long id, Section section) {
        return saveSection(section);
    }
}
