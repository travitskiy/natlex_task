package ru.natlex.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "geo")
public class Geo implements Serializable {
    private @Id
    @GeneratedValue(generator = "geo_id_generator")
    Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;

    public Geo() { }

    public Geo(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @JsonIgnore
    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geo geo = (Geo) o;
        return Objects.equals(id, geo.id) &&
                Objects.equals(section, geo.section) &&
                Objects.equals(name, geo.name) &&
                Objects.equals(code, geo.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}
