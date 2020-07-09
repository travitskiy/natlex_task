package ru.natlex.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natlex.task.model.Section;

import java.util.Optional;

public interface SectionsRepository extends JpaRepository<Section, Long> {
    Optional<Section> findSectionByHash(int hash);
}
