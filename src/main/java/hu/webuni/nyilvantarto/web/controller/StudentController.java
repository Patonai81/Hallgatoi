package hu.webuni.nyilvantarto.web.controller;


import hu.webuni.nyilvantarto.dto.StudentDTO;
import hu.webuni.nyilvantarto.mapper.StudentMapper;
import hu.webuni.nyilvantarto.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/student")
@RestController("StundentController")
public class StudentController{

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentMapper studentMapper;

    @GetMapping("/find/{id}")
    public StudentDTO findStudentById(@PathVariable("id") Long id){
        return  studentMapper.toStudentDTO(studentRepository.findStudentByOwnId(id));
    }
}
