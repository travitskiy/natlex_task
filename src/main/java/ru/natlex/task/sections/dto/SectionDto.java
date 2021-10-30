package ru.natlex.task.sections.dto;

import ru.natlex.task.geologicalglass.dto.GeologicalClassDto;

import java.util.List;

public class SectionDto {

    private Long id;
    private String name;
    private List<GeologicalClassDto> geologicalClasses;

    public SectionDto(Long id, String name, List<GeologicalClassDto> geologicalClasses) {
        this.id = id;
        this.name = name;
        this.geologicalClasses = geologicalClasses;
    }

    public List<GeologicalClassDto> getGeologicalClasses() {
        return geologicalClasses;
    }

    public void setGeologicalClasses(List<GeologicalClassDto> geologicalClasses) {
        this.geologicalClasses = geologicalClasses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
