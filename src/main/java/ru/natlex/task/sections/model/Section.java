package ru.natlex.task.sections.model;

import ru.natlex.task.geologicalglass.model.GeologicalClass;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GeologicalClass> geologicalClassList = new ArrayList<>();

    public Section() { }

    public Section(String name) {
        this.name = name;
    }

    public Section(String name, List<GeologicalClass> geologicalClassList) {
        this.name = name;
        this.geologicalClassList = geologicalClassList;
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

    public List<GeologicalClass> getGeoList() {
        return geologicalClassList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return name.equals(section.name) &&
                geologicalClassList.equals(section.geologicalClassList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, geologicalClassList);
    }

}
