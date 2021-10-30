package ru.natlex.task.sections.dto;

import java.util.List;

public class SectionsResponse {

    private List<SectionDto> sections;

    public SectionsResponse(List<SectionDto> sections) {
        this.sections = sections;
    }

    public List<SectionDto> getSections() {
        return sections;
    }

    public void setSections(List<SectionDto> sections) {
        this.sections = sections;
    }
}
