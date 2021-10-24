package ru.natlex.task.geologicalglass.dto;

import javax.validation.constraints.NotEmpty;

public class GeologicalClassDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String code;

    public GeologicalClassDto(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
