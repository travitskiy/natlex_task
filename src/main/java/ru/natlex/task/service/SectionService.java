package ru.natlex.task.service;

import ru.natlex.task.model.AsyncJob;
import ru.natlex.task.model.Section;
import ru.natlex.task.model.SectionsExportFile;

import java.util.List;
import java.util.Optional;

public interface SectionService {

    List<Section> getAllSections();

    Section getSection(Long id);

    List<Section> getSectionsConsistCode(String code);

    Optional<Section> saveSection(Section section);

    void deleteSection(Section section);

    Optional<Section>  updateSection(Long id, Section section);

    Optional<SectionsExportFile> findExportedFile(AsyncJob asyncJob);

    SectionsExportFile saveExportFile(SectionsExportFile result);

}
