package ru.natlex.task.sections.dto;

import ru.natlex.task.geologicalglass.dto.GeologicalClassDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SectionCreateRequest {

    @NotEmpty(message = "Section name is required")
    private String name;
    @NotNull
    private List<GeologicalClassDto> geologicalClasses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GeologicalClassDto> getGeologicalClasses() {
        return geologicalClasses;
    }

    public void setGeologicalClasses(List<GeologicalClassDto> geologicalClasses) {
        this.geologicalClasses = geologicalClasses;
    }
}
