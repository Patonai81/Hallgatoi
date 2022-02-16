package hu.webuni.nyilvantarto.mapper;

import hu.webuni.nyilvantarto.dto.StudentDTO;
import hu.webuni.nyilvantarto.model.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDTO toStudentDTO(Student student);

    Student toStudent(StudentDTO studentDTO);

}
