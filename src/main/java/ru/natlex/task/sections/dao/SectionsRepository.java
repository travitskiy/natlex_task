package ru.natlex.task.sections.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.natlex.task.sections.model.Section;

import java.util.List;

public interface SectionsRepository extends JpaRepository<Section, Long> {
    @Query(
            value = "SELECT * FROM SECTIONS INNER JOIN GEOLOGICAL_CLASS ON SECTION_ID = SECTIONS.ID WHERE Code = ?",
            nativeQuery = true)
    List<Section> getSectionsByGeoCode(String code);
}
