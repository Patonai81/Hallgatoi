package hu.webuni.nyilvantarto.web.controller;


import hu.webuni.nyilvantarto.dto.StudentDTO;
import hu.webuni.nyilvantarto.mapper.StudentMapper;
import hu.webuni.nyilvantarto.repository.StudentRepository;
import hu.webuni.nyilvantarto.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/student")
@RestController("StundentController")
public class StudentController{

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    StudentService studentService;

    @GetMapping("/find/{id}")
    public StudentDTO findStudentById(@PathVariable("id") Long id){
        return  studentMapper.toStudentDTO(studentRepository.findStudentByOwnId(id));
    }

    @PostMapping("/add")
    public StudentDTO addStudent(@RequestBody StudentDTO studentDTO) {
        return studentMapper.toStudentDTO(studentService.insert(studentMapper.toStudent(studentDTO)));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStudentById(@PathVariable("id") Long id){
        studentService.delete(id);
        System.out.println("Operation delete successfully done");
    }

}
