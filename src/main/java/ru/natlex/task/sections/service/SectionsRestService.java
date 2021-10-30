package ru.natlex.task.sections.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natlex.task.sections.dto.SectionUpdateRequest;
import ru.natlex.task.sections.dto.SectionsResponse;
import ru.natlex.task.sections.model.Section;
import ru.natlex.task.sections.mapper.SectionsMapper;
import ru.natlex.task.sections.dto.SectionCreateRequest;
import ru.natlex.task.sections.dto.SectionCreatedResponse;

import java.util.List;

@Service
public class SectionsRestService {
    private final SectionsMapper sectionsMapper;
    private final SectionsService sectionsService;

    public SectionsRestService(SectionsMapper sectionsMapper, SectionsService sectionsService) {
        this.sectionsMapper = sectionsMapper;
        this.sectionsService = sectionsService;
    }

    @Transactional
    public SectionCreatedResponse createSection(SectionCreateRequest sectionCreateRequest) throws Exception {
        Section section = sectionsService.saveSection(sectionsMapper.createSection(sectionCreateRequest)).orElseThrow(() -> new Exception("Error Save Section"));
        return sectionsMapper.createSectionCreatedResponse(section);
    }

    @Transactional(readOnly = true)
    public SectionsResponse getAllSections() {
        List<Section> sections = sectionsService.getAllSections();
        return sectionsMapper.createSectionsResponse(sections);
    }

    @Transactional(readOnly = true)
    public SectionsResponse getSectionsByGeoCode(String code) {
        List<Section> sections = sectionsService.getSectionsByGeoCode(code);
        return sectionsMapper.createSectionsResponse(sections);
    }

//    @Transactional
//    public SectionCreatedResponse updateSection(SectionUpdateRequest sectionUpdateRequest) {
//        Section section = sectionsService.updateSection(sectionsMapper.createSection(sectionUpdateRequest));
//        return sectionsMapper.createSectionCreatedResponse(section);
//    }
}
