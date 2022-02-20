package hu.webuni.nyilvantarto.web.controller;



import hu.webuni.nyilvantarto.mapper.TeacherMapper;
import hu.webuni.nyilvantarto.model.TeacherDTO;
import hu.webuni.nyilvantarto.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/teacher")
//@RestController("TeacherController")
public class TeacherController_Old {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    TeacherMapper teacherMapper;

    @GetMapping("/find/{id}")
    public TeacherDTO findStudentById(@PathVariable("id") Long id){
        return  teacherMapper.toTeacherDTO(teacherRepository.findById(id).get());
    }
}
