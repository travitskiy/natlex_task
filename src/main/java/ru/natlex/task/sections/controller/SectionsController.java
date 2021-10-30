package ru.natlex.task.sections.controller;

import org.springframework.web.bind.annotation.*;
import ru.natlex.task.sections.dto.SectionCreateRequest;
import ru.natlex.task.sections.dto.SectionCreatedResponse;
import ru.natlex.task.sections.dto.SectionUpdateRequest;
import ru.natlex.task.sections.dto.SectionsResponse;
import ru.natlex.task.sections.service.SectionsRestService;
import ru.natlex.task.sections.service.SectionsService;

import javax.validation.Valid;

@RestController
@RequestMapping("/sections")
public class SectionsController {

    private final SectionsRestService sectionsRestService;

    public SectionsController(SectionsRestService sectionsRestService) {
        this.sectionsRestService = sectionsRestService;
    }

    @PostMapping("/create")
    public SectionCreatedResponse createSection(@Valid @RequestBody SectionCreateRequest sectionCreateRequest) throws Exception {
        return sectionsRestService.createSection(sectionCreateRequest);
    }

    @PostMapping("/update")
    public SectionCreatedResponse updateSection(@Valid @RequestBody SectionUpdateRequest sectionUpdateRequest) throws Exception {
        return sectionsRestService.updateSection(sectionUpdateRequest);
    }

    @GetMapping("/all")
    public SectionsResponse getAllSections() {
        return sectionsRestService.getAllSections();
    }

    @GetMapping("/by-code")
    public SectionsResponse getSectionsByGeoCode(@RequestParam String code) {
        return sectionsRestService.getSectionsByGeoCode(code);
    }
}
