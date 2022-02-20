package hu.webuni.nyilvantarto.web.controller;

import hu.webuni.nyilvantarto.api.StudentControllerApi;
import hu.webuni.nyilvantarto.mapper.StudentMapper;
import hu.webuni.nyilvantarto.model.StudentDTO;
import hu.webuni.nyilvantarto.repository.StudentRepository;
import hu.webuni.nyilvantarto.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudentController implements StudentControllerApi {


    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    StudentService studentService;

    @Override
    public ResponseEntity<StudentDTO> addStudent(StudentDTO studentDTO) {
        return ResponseEntity.ok(studentMapper.toStudentDTO(studentService.insert(studentMapper.toStudent(studentDTO))));
    }

    @Override
    public ResponseEntity<Void> deleteStudentById(Long id) {
        studentService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<StudentDTO> findStudentById1(Long id) {
        return ResponseEntity.ok(studentMapper.toStudentDTO(studentRepository.findStudentByOwnId(id)));
    }
}
