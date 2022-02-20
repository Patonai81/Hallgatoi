package hu.webuni.nyilvantarto.mapper;


import hu.webuni.nyilvantarto.model.Teacher;
import hu.webuni.nyilvantarto.model.TeacherDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherDTO toTeacherDTO(Teacher teacher);
}
