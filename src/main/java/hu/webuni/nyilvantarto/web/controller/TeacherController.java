package hu.webuni.nyilvantarto.web.controller;

import hu.webuni.nyilvantarto.api.TeacherControllerApi;
import hu.webuni.nyilvantarto.mapper.TeacherMapper;
import hu.webuni.nyilvantarto.model.TeacherDTO;
import hu.webuni.nyilvantarto.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeacherController implements TeacherControllerApi {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    TeacherMapper teacherMapper;

    @Override
    public ResponseEntity<TeacherDTO> findStudentById(Long id) {
        return  ResponseEntity.ok(teacherMapper.toTeacherDTO(teacherRepository.findById(id).get()));
    }
}
