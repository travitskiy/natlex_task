package ru.natlex.task.sections.dto;

public class SectionCreatedResponse {

    private Long sectionId;

    public SectionCreatedResponse(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }
}
