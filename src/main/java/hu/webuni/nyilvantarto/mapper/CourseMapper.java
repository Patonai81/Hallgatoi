package hu.webuni.nyilvantarto.mapper;

import hu.webuni.nyilvantarto.dto.CourseDTO;
import hu.webuni.nyilvantarto.model.Course;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel ="spring")
public interface CourseMapper {

    List<Course> toCourseList(List<CourseDTO> input);
    Course toCourse(CourseDTO input);

    @IterableMapping(qualifiedByName = "full")
    List<CourseDTO> toCourseDTOListFull(List<Course> input);

    @IterableMapping(qualifiedByName = "basic")
    List<CourseDTO> toCourseDTOListBasic(List<Course> input);

    @Named("basic")
    @Mapping(target = "studentList", ignore = true)
    @Mapping(target = "teacherList", ignore = true)
    CourseDTO toCourseDTO(Course input);

    @Named("full")
    CourseDTO toCourseDTOFull(Course input);



}
