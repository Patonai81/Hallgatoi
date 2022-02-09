package hu.webuni.nyilvantarto.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CourseDTO {

    private Long course_id;
    private String name;
    private Set<StudentDTO> studentList;
    private Set<TeacherDTO> teacherList;

}
