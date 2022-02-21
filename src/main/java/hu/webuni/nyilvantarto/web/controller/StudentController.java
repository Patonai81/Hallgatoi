package hu.webuni.nyilvantarto.web.controller;

import hu.webuni.nyilvantarto.api.StudentControllerApi;
import hu.webuni.nyilvantarto.mapper.StudentMapper;
import hu.webuni.nyilvantarto.model.StudentDTO;
import hu.webuni.nyilvantarto.repository.StudentRepository;
import hu.webuni.nyilvantarto.repository.file.File;
import hu.webuni.nyilvantarto.repository.file.FileContentStore;
import hu.webuni.nyilvantarto.repository.file.FileRepository;
import hu.webuni.nyilvantarto.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StudentController implements StudentControllerApi {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    StudentService studentService;

    @Autowired
    private FileRepository filesRepo;
    @Autowired
    private FileContentStore contentStore;


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

    @Override
    public ResponseEntity<String> uploadStudentImage(Long id, String fileName, MultipartFile content) {

        Optional<File> f = filesRepo.findById(id);
        File file = null;

        if (f.isPresent()) {
            f.get().setContentMimeType(content.getContentType());
            file = f.get();
        } else {
            file = new File();
            file.setId(id);
            file.setContentId(fileName);
            file.setContentLength(content.getSize());
            file.setContentMimeType(content.getContentType());
            file = filesRepo.save(file);
        }

        try {
            contentStore.setContent(file, content.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // save updated content-related info
        filesRepo.save(file);

        return ResponseEntity.ok("Az entitás a következő idval érhető el: "+file.getId());


    }
}