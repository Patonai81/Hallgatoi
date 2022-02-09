package hu.webuni.nyilvantarto.mapper;

import hu.webuni.nyilvantarto.dto.TeacherDTO;
import hu.webuni.nyilvantarto.model.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherDTO toTeacherDTO(Teacher teacher);
}
