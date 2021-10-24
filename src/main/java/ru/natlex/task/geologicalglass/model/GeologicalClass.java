package ru.natlex.task.geologicalglass.model;

import ru.natlex.task.sections.model.Section;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "geological_class")
public class GeologicalClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    public GeologicalClass() { }

    public GeologicalClass(String name, String code) {
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
        GeologicalClass geologicalClass = (GeologicalClass) o;
        return Objects.equals(id, geologicalClass.id) &&
                Objects.equals(section, geologicalClass.section) &&
                Objects.equals(name, geologicalClass.name) &&
                Objects.equals(code, geologicalClass.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}
