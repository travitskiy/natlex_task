package ru.natlex.task.sections.mapper;

import org.springframework.stereotype.Component;
import ru.natlex.task.geologicalglass.mapper.GeologicalClassMapper;
import ru.natlex.task.geologicalglass.dto.GeologicalClassDto;
import ru.natlex.task.sections.dto.SectionDto;
import ru.natlex.task.sections.dto.SectionsResponse;
import ru.natlex.task.geologicalglass.model.GeologicalClass;
import ru.natlex.task.sections.model.Section;
import ru.natlex.task.sections.dto.SectionCreateRequest;
import ru.natlex.task.sections.dto.SectionCreatedResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SectionsMapper {

    private final GeologicalClassMapper geologicalClassMapper;

    public SectionsMapper(GeologicalClassMapper geologicalClassMapper) {
        this.geologicalClassMapper = geologicalClassMapper;
    }

    public Section createSection(SectionCreateRequest sectionCreateRequest) {
        Section section = new Section(
                sectionCreateRequest.getName(),
                new ArrayList<>()
        );

        List<GeologicalClass> geologicalClasses = sectionCreateRequest.getGeologicalClasses().stream().map(gc -> {
            GeologicalClass geologicalClass = new GeologicalClass(gc.getName(), gc.getCode());
            geologicalClass.setSection(section);
            return geologicalClass;
        }).collect(Collectors.toList());

        section.getGeoList().addAll(geologicalClasses);

        return section;
    }

    public SectionCreatedResponse createSectionCreatedResponse(Section section) {
        return new SectionCreatedResponse(section.getId());
    }

    public SectionsResponse createSectionsResponse(List<Section> sections) {
        List<SectionDto> sectionDtos = sections.stream().map(s -> new SectionDto(
                        s.getId(),
                        s.getName(),
                        s.getGeoList().stream()
                                .map(gc -> new GeologicalClassDto(gc.getName(), gc.getCode()))
                                .collect(Collectors.toList())
                )
        ).collect(Collectors.toList());

        return new SectionsResponse(sectionDtos);
    }
}
