package ru.natlex.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sections")
public class Section {
    private @Id
    @GeneratedValue(generator = "sections_id_generator")
    Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Geo> geoList = new ArrayList<>();

    @JsonIgnore
    @Column(name = "hash", unique = true)
    private int hash;

    public Section() { }

    public Section(String name) {
        this.name = name;
    }

    public Section(String name, List<Geo> geoList) {
        this.name = name;
        this.geoList = geoList;
        for(Geo geo : geoList)
            geo.setSection(this);
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

    public List<Geo> getGeoList() {
        return Collections.unmodifiableList(geoList);
    }

    public void addGeo(Geo geo) {
        geoList.add(geo);
        geo.setSection(this);
    }

    public void removeGeo(Geo geo) {
        geoList.remove(geo);
        geo.setSection(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return name.equals(section.name) &&
                geoList.equals(section.geoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, geoList);
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    @PrePersist
    public void prePersist() {
        hash = hashCode();
    }

    @PreUpdate
    public void preUpdate() {
        hash = hashCode();
    }
}
